package com.example.corecard.service;
import com.example.corecard.client.CoreBankingClient;
import com.example.corecard.client.CryptoClient;
import com.example.corecard.entity.ActivityStatusEntity;
import com.example.corecard.entity.CardProductEntity;
import com.example.corecard.entity.CardsEntity;
import com.example.corecard.entity.CustomersEntity;
import com.example.corecard.repository.ActivityStatusRepository;
import com.example.corecard.repository.CardProductRepository;
import com.example.corecard.repository.CardsRepository;
import com.example.corecard.repository.CustomersRepository;
import com.example.corecard.request.CheckAccountExistRequest;
import com.example.corecard.request.CheckAccountValidRequest;
import com.example.corecard.request.CryptoRequest;
import com.example.corecard.request.InsertCardRequest;
import com.example.corecard.response.AccountInfoResponse;
import com.example.corecard.response.BaseResponse;
import com.example.corecard.response.CheckAccountValidResponse;
import com.example.corecard.response.Corebank.CoreBankAccountResponse;
import com.example.corecard.response.InsertCardResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Locale;


@Slf4j
@Service
public class CardsService {
    private CardsRepository cardsRepository;
    private CoreBankingClient coreBankingClient;
    private CryptoClient cryptoClient;
    private CustomersRepository customersRepository;
    private GenerateService generateService;
    private CardProductRepository cardProductRepository;
    private ActivityStatusRepository activityStatusRepository;

    public CardsService(CardsRepository cardsRepository, CoreBankingClient coreBankingClient, CryptoClient cryptoClient, CustomersRepository customersRepository, GenerateService generateService, CardProductRepository cardProductRepository, ActivityStatusRepository activityStatusRepository){
        this.coreBankingClient = coreBankingClient;
        this.cardsRepository = cardsRepository;
        this.cryptoClient = cryptoClient;
        this.customersRepository = customersRepository;
        this.generateService = generateService;
        this.cardProductRepository = cardProductRepository;
        this.activityStatusRepository = activityStatusRepository;
    }

    public String getCustomerCode(String cardNumberHash){
        try {
            CardsEntity card = new CardsEntity();
            card = cardsRepository.findByCardNumberHash(cardNumberHash);
            String customerCode = card.getCustomers().getCustomerCode();
            return customerCode;
        } catch (Exception e) {
            throw new RuntimeException("Loi khong tim thay customer code: " + e.getMessage());
        }
    }

    public AccountInfoResponse getAccountNumberAndCustomerCode(String cardNumberHash){
        try {
            CardsEntity card = cardsRepository.findByCardNumberHash(cardNumberHash);
            AccountInfoResponse accountInfoResponse = new AccountInfoResponse();
            accountInfoResponse.setAccountNumber(card.getAccountNumber());
            accountInfoResponse.setCustomerCode(card.getCustomers().getCustomerCode());
            return accountInfoResponse;
        }catch (Exception e){
            throw new RuntimeException("loi khong tim thay info account" + e.getMessage());
        }
    }

    public CoreBankAccountResponse checkAccountExistByPhoneAndNumber(InsertCardRequest request){
        return coreBankingClient.checkAccountExistByNameAndPhone(request);
    }

    public CheckAccountValidResponse checkCoreBankingAccount(String cardNumber, String cvv, BigDecimal amount){
        try {
            CryptoRequest cardNumberHashRequest = new CryptoRequest(cardNumber);
            CryptoRequest cvvHashRequest = new CryptoRequest(cvv);

            String cardNumberHash = cryptoClient.hashing(cardNumberHashRequest);
            String cvvHash = cryptoClient.hashing(cvvHashRequest);

            CardsEntity card = cardsRepository.findByCardNumberHashAndCvvHash(cardNumberHash,cvvHash);
            if (card == null) {
                return new CheckAccountValidResponse("invalid_card", false, "Thông tin thẻ không hợp lệ");
            }
            CheckAccountValidRequest checkAccountRequest = new CheckAccountValidRequest();
            checkAccountRequest.setAccountNumber(card.getAccountNumber());
            checkAccountRequest.setCustomerCode(card.getCustomers().getCustomerCode());
            checkAccountRequest.setAmount(amount);
            return coreBankingClient.checkAccountValid(checkAccountRequest);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public InsertCardResponse insertCard(InsertCardRequest request) {
        String fullName = request.getFullName();
        String phone = request.getPhone();
        String cardType = request.getCardProductType();
        String customerCode = null;
        String accountNumber = null;
        String email = null;

        fullName = fullName.strip();
        phone = phone.strip();
        cardType = cardType.strip().toUpperCase(Locale.ROOT);

        CustomersEntity customer = new CustomersEntity();

        //neu la credit thi phai kiem tra ng dung co dky tai khoan ben core bank chua?
        if ("DEBIT_BASIC".equals(cardType)) {
            CoreBankAccountResponse response = coreBankingClient.checkAccountExistByNameAndPhone(request);
            if (response.getData() == null) {
                return new InsertCardResponse("Vui lòng mở tài khoản trước khi tạo thẻ Debit", "invalid");
            }

            accountNumber = response.getData().getAccountNumber();
            if (accountNumber == null || accountNumber.isBlank()
                    || response.getData().getCustomer() == null) {
                throw new IllegalStateException("Corebank trả thiếu thông tin tài khoản");
            }

            customerCode = response.getData().getCustomer().getCustomerCode();
            if (customerCode == null || customerCode.isBlank()) {
                throw new IllegalStateException("Corebank trả thiếu mã khách hàng");
            }
            fullName = response.getData().getCustomer().getFullName();
            phone = response.getData().getCustomer().getPhone();
            email = response.getData().getCustomer().getEmail();
            customer = customersRepository.findByCustomerCode(customerCode);
        }

        CardProductEntity cardProduct = cardProductRepository.findByCardProductType(cardType);
        ActivityStatusEntity activityStatus = activityStatusRepository.findByCode("ACTIVE");


        // Hai loại thẻ dùng chung phần sinh, băm và mã hóa PAN/CVV.
        String pan = generateService.generatePan(cardsRepository.nextPanNumber());
        String cvv = generateService.generateCvv();

        String panHash = cryptoClient.hashing(new CryptoRequest(pan));
        String cvvHash = cryptoClient.hashing(new CryptoRequest(cvv));
        String panEncrypted = cryptoClient.encrypt(new CryptoRequest(pan));
        String cvvEncrypted = cryptoClient.encrypt(new CryptoRequest(cvv));
        LocalDateTime now = LocalDateTime.now();

        // Chỉ tạo hồ sơ khi Corecard chưa có khách hàng.
        if (customer == null) {
            if ("CREDIT_BASIC".equals(cardType)) {
                customerCode = "CC" + customersRepository.nextCreditCustomerNumber();
            }

            customer = new CustomersEntity();
            customer.setCustomerCode(customerCode);
            customer.setFullName(fullName);
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setCreatedAt(now);
            customer = customersRepository.save(customer);
        }

        CardsEntity card = new CardsEntity();
        card.setAccountNumber(accountNumber);
        card.setCustomers(customer);
        card.setCardProduct(cardProduct);
        card.setActivityStatus(activityStatus);
        card.setCardNumberHash(panHash);
        card.setCvvHash(cvvHash);
        card.setCardNumberEncrypted(panEncrypted);
        card.setCvvEncrypted(cvvEncrypted);
        card.setExpiredDate(YearMonth.from(now).plusYears(3).atEndOfMonth());
        card.setCreatedAt(now);

        if ("CREDIT_BASIC".equals(cardType)) {
            card.setCreditLimit(BigDecimal.valueOf(15_000_000));
        }

        cardsRepository.save(card);

        return new InsertCardResponse("Tạo thẻ thành công", "success");
    }

}
