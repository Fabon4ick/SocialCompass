package com.example.socialcompass;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Registration extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private EditText birthdayEditText;
    private EditText dateOfIssueEditText; // Добавляем переменную для даты выдачи

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        // Настраиваем поля с ограничением по символам
        setupEditTextWithLimit(R.id.nameEditText, 80);
        setupEditTextWithLimit(R.id.surnameEditText, 80);
        setupEditTextWithLimit(R.id.patronymicEditText, 80);
        setupEditTextWithLimit(R.id.issuedByEditText, 80);

        // Настраиваем поле с маской для номера телефона
        setupPhoneNumberMask(R.id.phoneNumberEditText);

        // Настраиваем выбор даты рождения
        birthdayEditText = findViewById(R.id.birthdayEditText);
        disableKeyboardInput(birthdayEditText); // Отключаем клавиатуру
        birthdayEditText.setOnClickListener(v -> showDatePickerDialog(birthdayEditText));

        // Настраиваем выбор даты для "Кем выдан"
        dateOfIssueEditText = findViewById(R.id.dateOfIssueEditText);
        disableKeyboardInput(dateOfIssueEditText); // Отключаем клавиатуру
        dateOfIssueEditText.setOnClickListener(v -> showDatePickerDialog(dateOfIssueEditText));

        // Устанавливаем обработчик для выбора фотографии
        TextView selectPhotoTextView = findViewById(R.id.selectPhotoTextView);
        selectPhotoTextView.setOnClickListener(v -> openFileChooser());

        ImageView imageView = findViewById(R.id.imageView1);

        int imageResource = R.drawable.background;

        Glide.with(this).load(imageResource).apply(new RequestOptions().override(Target.SIZE_ORIGINAL, 550)).into(imageView);
    }

    // Отключение клавиатуры для поля даты
    private void disableKeyboardInput(EditText editText) {
        editText.setFocusable(false); // Убираем возможность ввода с клавиатуры
        editText.setClickable(true);  // Делаем поле кликабельным для вызова календаря
    }

    // Метод для открытия проводника для выбора изображения
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Выбираем только изображения
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Обработка результата выбора изображения
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData(); // Получаем URI выбранного изображения
            Toast.makeText(this, "Изображение выбрано", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка выбора изображения", Toast.LENGTH_SHORT).show();
        }
    }

    // Метод для отображения диалога выбора даты
    private void showDatePickerDialog(EditText targetEditText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Устанавливаем выбранную дату в нужный EditText
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    targetEditText.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Метод для установки ограничения по количеству символов
    private void setupEditTextWithLimit(int editTextId, int maxLength) {
        EditText editText = findViewById(editTextId);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    // Метод для установки маски на номер телефона
    private void setupPhoneNumberMask(int editTextId) {
        EditText phoneNumberEditText = findViewById(editTextId);
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private final String defaultPrefix = "+7 ";  // Префикс для номера телефона

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    return;
                }
                isUpdating = true;

                String phone = s.toString().replaceAll("[^\\d]", "");  // Убираем все символы, кроме цифр
                StringBuilder formatted = new StringBuilder();

                if (phone.length() >= 1) {
                    formatted.append("+7 ");
                }

                if (phone.length() > 1) {
                    formatted.append("(");
                    formatted.append(phone.substring(1, Math.min(phone.length(), 4)));
                }

                if (phone.length() >= 4) {
                    formatted.append(") ");
                    formatted.append(phone.substring(4, Math.min(phone.length(), 7)));
                }

                if (phone.length() >= 7) {
                    formatted.append("-");
                    formatted.append(phone.substring(7, Math.min(phone.length(), 9)));
                }

                if (phone.length() >= 9) {
                    formatted.append("-");
                    formatted.append(phone.substring(9, Math.min(phone.length(), 11)));
                }

                phoneNumberEditText.setText(formatted.toString());
                phoneNumberEditText.setSelection(phoneNumberEditText.getText().length());
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
