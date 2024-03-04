package g313.vakulenko.hw4_3_lms;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // Поля
    private TextView screen, coordinates, keysFound;
    private float x;
    private float y;
    private int totalKeys = 5; // Общее количество ключей
    private Set<Integer> foundKeys = new HashSet<>(); // Множество для хранения найденных ключей

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Привязка элементов интерфейса к соответствующим полям
        screen = findViewById(R.id.screen);
        coordinates = findViewById(R.id.coordinates);
        keysFound = findViewById(R.id.keys_found);

        // Установка обработчика нажатия кнопки "Генерировать ключи"
        Button generateButton = findViewById(R.id.generate_button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomCoordinates();
            }
        });

        // Обработка касаний TextView
        screen.setOnTouchListener(listener);
    }

    // Генерация случайных координат для ключей
    private void generateRandomCoordinates() {
        foundKeys.clear(); // Очистка найденных ключей перед генерацией новых
        Random random = new Random();
        while (foundKeys.size() < totalKeys) {
            int keyX = random.nextInt(screen.getWidth()); // Генерация случайной координаты X
            int keyY = random.nextInt(screen.getHeight()); // Генерация случайной координаты Y
            int key = getKeyFromCoordinates(keyX, keyY); // Получение ключа по координатам
            foundKeys.add(key); // Добавление ключа в множество найденных
        }
        updateKeysFoundText(); // Обновление текста о найденных ключах
    }

    // Обработчик событий касания экрана
    private View.OnTouchListener listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            x = motionEvent.getX();
            y = motionEvent.getY();

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    coordinates.setText("Нажатие " + x + ", " + y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    coordinates.setText("Движение " + x + ", " + y);
                    int key = getKeyFromCoordinates(x, y); // Получение ключа по координатам
                    if (key != -1 && foundKeys.contains(key)) { // Проверка на наличие ключа
                        foundKeys.remove(key); // Удаление найденного ключа из множества
                        Toast.makeText(MainActivity.this, "Найден ключ " + key, Toast.LENGTH_SHORT).show();
                        updateKeysFoundText(); // Обновление текста о найденных ключах
                        if (foundKeys.isEmpty()) { // Если все ключи найдены
                            Toast.makeText(MainActivity.this, "Все ключи найдены!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    coordinates.setText("Отпускание " + x + ", " + y);
                    break;
            }

            return true;
        }
    };

    // Получение ключа по координатам
    private int getKeyFromCoordinates(float x, float y) {
        int keyX = (int) (x / (screen.getWidth() / 5)); // Вычисление координаты X ключа
        int keyY = (int) (y / (screen.getHeight() / 5)); // Вычисление координаты Y ключа
        return (keyY * 5) + keyX + 1; // Возврат номера ключа
    }




    // Обновление текста о найденных ключах
    private void updateKeysFoundText() {
        keysFound.setText("Найденные ключи: " + (totalKeys - foundKeys.size()) + " из " + totalKeys);
    }
}