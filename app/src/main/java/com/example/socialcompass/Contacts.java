package com.example.socialcompass;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Contacts extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        // Найти TextView по его ID
        TextView phoneNumberTextView = findViewById(R.id.phoneNumberTextView);

        // Установить обработчик нажатия на TextView
        phoneNumberTextView.setOnClickListener(v -> {
            // Получить номер телефона из TextView
            String phoneNumber = phoneNumberTextView.getText().toString();

            // Создать Intent для открытия приложения Телефон с введенным номером
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));

            // Запуск приложения Телефон
            startActivity(intent);
        });
    }
}
