package btill.terminal.values;


import com.google.protobuf.InvalidProtocolBufferException;
import org.bitcoin.protocols.payments.Protos;
import org.bitcoinj.core.Coin;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.protocols.payments.PaymentProtocol;
import org.bitcoinj.uri.BitcoinURI;
import org.bitcoinj.uri.BitcoinURIParseException;

import java.io.Serializable;

public class NewBill implements Serializable {
    private byte[] request;
    private GBP amount;

    public NewBill(GBP amount) {
        this.amount = amount;
    }

    public Protos.PaymentRequest getRequest() {
        try {
            return Protos.PaymentRequest.parseFrom(request);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setRequest(Protos.PaymentRequest request) {
        this.request = request.toByteArray();
    }

    public Protos.PaymentRequest getRequest(String uri) throws BitcoinURIParseException {
        BitcoinURI mUri = new BitcoinURI(TestNet3Params.get(), uri);
        org.bitcoinj.core.Address address = mUri.getAddress();
        Coin amount = mUri.getAmount();
        String memo = mUri.getMessage();
        String url = mUri.getPaymentRequestUrl();
        Protos.PaymentRequest.Builder requestBuilder = PaymentProtocol.createPaymentRequest(TestNet3Params.get(),
                amount, address, memo, url, null);
        Protos.PaymentRequest request = requestBuilder.build();
        return request;
    }

    @Override
    public String toString() {
        return new String("Bill for " + amount.toString());
    }

}
