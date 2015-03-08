package btill.terminal.values;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoin.protocols.payments.Protos.PaymentRequest;
import org.bitcoin.protocols.payments.Protos.PaymentRequest.Builder;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.protocols.payments.PaymentProtocol;
import org.bitcoinj.protocols.payments.PaymentProtocolException;
import org.bitcoinj.protocols.payments.PaymentSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Bill acts as a wrapper class for a {@link org.bitcoin.protocols.payments.Protos.PaymentRequest}. It is constructed
 * by the {@link btill.terminal.bitcoin.BitcoinTill} to the Phone in response to an {@link Order}.
 */
public class Bill implements Serializable {

    protected static final Logger Log = LoggerFactory.getLogger(Bill.class);
    private static final long serialVersionUID = 8676831317792422678L;

    // TRANSIENT VARIABLES - NOT TRANSMITTED TO PHONE OR BACK
    private transient TestNet3Params net3Params = TestNet3Params.get();
    private transient String memo = null;
    private transient String paymentURL = null;
    private transient byte[] merchantData = null;
    private transient Wallet wallet = null;
    private transient PaymentRequest paymentRequest = null;

    private GBP gbpAmount = null;
    private byte[] request = null;
    private Coin coinAmount = null;

    @Override
    public String toString() {
        return String.format("Bill for " + coinAmount.toFriendlyString());
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public Bill(String memo, String paymentURL, byte[] merchantData,
                Coin amount, GBP gbpAmount, Wallet wallet, Boolean freshAddress) {
        this.memo = memo;
        this.paymentURL = paymentURL;
        this.merchantData = merchantData;
        this.coinAmount = amount;
        this.gbpAmount = gbpAmount;
        this.wallet = wallet;
        buildPaymentRequest(freshAddress);
    }

    public Wallet getWallet() {
        return wallet;
    }

    public String getMemo() {
        return memo;
    }

    public String getPaymentURL() {
        return paymentURL;
    }

    public byte[] getMerchantData() {
        return merchantData;
    }

    public GBP getGbpAmount() {
        return gbpAmount;
    }

    public Coin getCoinAmount() {
        return coinAmount;
    }

    public Protos.PaymentRequest getRequest() {
        try {
            return Protos.PaymentRequest.parseFrom(request);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public void buildPaymentRequest(Boolean freshAddress) {
        Builder requestBuilder;

        if (freshAddress)
            requestBuilder = PaymentProtocol.createPaymentRequest(net3Params,
                                coinAmount, wallet.freshReceiveAddress(), memo, paymentURL,
                                merchantData);
        else
            requestBuilder = PaymentProtocol.createPaymentRequest(net3Params,
                    coinAmount, wallet.currentReceiveAddress(), memo, paymentURL,
                    merchantData);

        paymentRequest = requestBuilder.build();
        request = paymentRequest.toByteArray();
    }

    /**
     * Pay this Bill using the {@link Wallet}
     */
    public SignedBill pay() {

        Protos.Payment payment = null;

        if (paymentRequest == null && request != null)
            try {
                paymentRequest = PaymentRequest.parseFrom(request);
            } catch (InvalidProtocolBufferException e) {
                Log.error("Could not rebuild Payment Request!");
                e.printStackTrace();
            }

        try {
            // Create a PaymentSession using the PaymentRequest for helpful
            // functions
            PaymentSession session = new PaymentSession(paymentRequest, false);

            Wallet.SendRequest sendRequest = session.getSendRequest();

            sendRequest.ensureMinRequiredFee = false;
            sendRequest.fee = Coin.ZERO;
            sendRequest.feePerKb = Coin.ZERO;
            sendRequest.signInputs = false;

            Log.info(sendRequest.tx.toString());

            wallet.completeTx(sendRequest);

            Log.info("COMPLETED: " + sendRequest.tx);

            wallet.signTransaction(sendRequest);

            Log.info("SIGNED: " + sendRequest.tx);

            List<Transaction> txil = new ArrayList<Transaction>();

            txil.add(sendRequest.tx);

            payment = session.getPayment(txil,wallet.currentReceiveAddress(),session.getMemo());

        } catch (PaymentProtocolException e) {
            Log.error("Payment Protocol Exception!");
            e.printStackTrace();
        } catch (InsufficientMoneyException e) {
            Log.error("Insufficient money to pay!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SignedBill(payment, gbpAmount, coinAmount);
    }

}
