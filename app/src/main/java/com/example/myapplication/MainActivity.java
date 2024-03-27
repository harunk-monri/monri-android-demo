package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCaller;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Supplier;

import com.example.myapplication.models.NewPaymentResponse;
import com.monri.android.Monri;
import com.monri.android.ResultCallback;
import com.monri.android.model.Card;
import com.monri.android.model.ConfirmPaymentParams;
import com.monri.android.model.CustomerParams;
import com.monri.android.model.DirectPayment;
import com.monri.android.model.MonriApiOptions;
import com.monri.android.model.PaymentMethodParams;
import com.monri.android.model.PaymentResult;
import com.monri.android.model.TransactionParams;
import com.monri.android.view.CardMultilineWidget;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements ResultCallback<PaymentResult> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Monri monri;
    private CardMultilineWidget monriCardWidget;
    private Button payButton;
    private Button payWithPayCekButton;
    private TextView result;
    private CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setupView();
        bindEvents();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void init() {
        monri = new Monri(((ActivityResultCaller) this),
                MonriApiOptions.create(Config.authenticityToken(), Config.isDevelopmentMode())
        );
    }

    private void setupView() {
        monriCardWidget = findViewById(R.id.monri_card_widget);
        checkbox = findViewById(R.id.checkbox);
        payButton = findViewById(R.id.pay);
        payWithPayCekButton = findViewById(R.id.button_pay_with_paycek);
        result = findViewById(R.id.result);
    }

    private void bindEvents() {
        payButton.setOnClickListener(view -> startCardPayment());
        payWithPayCekButton.setOnClickListener(view -> startPayCekPayment());
    }

    private void startCardPayment() {
        final Card card = monriCardWidget.getCard();
        if (card == null || !card.validateCard()) {
            return;
        }
        final PaymentMethodParams paymentMethodParams = card.setTokenizePan(checkbox.isChecked()).toPaymentMethodParams();
        proceedWithPayment(() -> paymentMethodParams);
    }

    private void startPayCekPayment() {
        proceedWithPayment(() -> new DirectPayment(DirectPayment.Provider.PAY_CEK_HR).toPaymentMethodParams());
    }

    private void proceedWithPayment(Supplier<PaymentMethodParams> paymentMethodParamsSupplier) {
        final Disposable subscription = OrderRepository.createPaymentSession(false)
                .subscribe(handlePaymentSessionResponse(paymentMethodParamsSupplier));

        compositeDisposable.add(subscription);
    }

    Consumer<NewPaymentResponse> handlePaymentSessionResponse(Supplier<PaymentMethodParams> paymentMethodParamsSupplier) {
        return newPaymentResponse -> {
            if (!"approved".equals(newPaymentResponse.getStatus())) {
                Toast.makeText(this, "Payment session create failed", Toast.LENGTH_LONG).show();
            } else {

                final CustomerParams customerParams = new CustomerParams()
                        .setAddress("Dzemala Bijedica 2")
                        .setFullName("Harun Kološ")
                        .setCity("Sarajevo")
                        .setZip("71000")
                        .setPhone("+38761000111")
                        .setCountry("BA")
                        .setEmail("monri_android_sdk@monri.com");

                monri.confirmPayment(ConfirmPaymentParams.create(
                        newPaymentResponse.getClientSecret(),
                        paymentMethodParamsSupplier.get(),
                        TransactionParams.create()
                                .set("order_info", "Monri Android SDK Payment Session")
                                .set(customerParams)
                ), (paymentResult, throwable) -> {
                    if (throwable != null) {
                        onError(throwable);
                    } else {
                        onSuccess(paymentResult);
                    }
                });

            }
        };
    }

    @Override
    public void onSuccess(final PaymentResult paymentResult) {
        Toast.makeText(this, String.format("Transaction processed with result %s", paymentResult.getStatus()), Toast.LENGTH_LONG).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onError(final Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
        result.setText("Payment failed");
    }
}
