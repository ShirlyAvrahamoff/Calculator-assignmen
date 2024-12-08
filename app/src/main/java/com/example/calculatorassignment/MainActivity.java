package com.example.calculatorassignment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import java.math.BigDecimal;
import java.math.RoundingMode;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView result;
    double num1 = 0;
    double num2 = 0;
    char operator = '\0';
    boolean isOperatorPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        result = findViewById(R.id.textViewResult);
        result.setText("");
    }

    public void numberFunction(View view) {
        Button button = (Button) view;
        String value = button.getText().toString();

        if (isOperatorPressed) {
            result.append(value);
        } else {
            result.append(value);
        }
    }

    public void decimalFunction(View view) {
        String currentText = result.getText().toString();

        if (currentText.isEmpty() || currentText.endsWith(" ") || currentText.endsWith(operator + "")) {
            result.append("0.");
        } else if (!currentText.contains(".")) {
            result.append(".");
        } else {
            String lastNumber = currentText.substring(currentText.lastIndexOf(" ") + 1);
            if (!lastNumber.contains(".")) {
                result.append(".");
            }
        }
    }

    public void operatorFunction(View view) {
        Button button = (Button) view;
        String currentText = result.getText().toString();

        if (!currentText.isEmpty()) {
            if (isOperatorPressed) {
                equalFunction(view);
            }

            operator = button.getText().toString().charAt(0);
            result.append(" " + operator + " ");
            isOperatorPressed = true;
        }
    }

    public void equalFunction(View view) {
        String currentText = result.getText().toString();

        if (isOperatorPressed && operator != '\0') {
            try {
                String[] parts = currentText.split(" ");

                BigDecimal calculationResult = new BigDecimal(parts[0]);
                for (int i = 1; i < parts.length; i += 2) {
                    String op = parts[i];
                    BigDecimal nextNumber = new BigDecimal(parts[i + 1]);

                    switch (op) {
                        case "+":
                            calculationResult = calculationResult.add(nextNumber);
                            break;
                        case "-":
                            calculationResult = calculationResult.subtract(nextNumber);
                            break;
                        case "X":
                            calculationResult = calculationResult.multiply(nextNumber);
                            break;
                        case "/":
                            if (nextNumber.compareTo(BigDecimal.ZERO) != 0) {
                                calculationResult = calculationResult.divide(nextNumber, 10, RoundingMode.HALF_UP); // Set precision
                            } else {
                                result.setText("Error"); // Division by zero
                                return;
                            }
                            break;
                        default:
                            result.setText("Error");
                            return;
                    }
                }

                // Display the result
                result.setText(calculationResult.stripTrailingZeros().toPlainString());

                // Reset for further calculations
                num1 = calculationResult.doubleValue();
                operator = '\0';
                isOperatorPressed = false;

            } catch (Exception e) {
                result.setText("Error");
            }
        }
    }

    public void clearFunction(View view) {
        result.setText("");
        resetCalculator();
    }

    private void resetCalculator() {
        num1 = 0;
        num2 = 0;
        operator = '\0';
        isOperatorPressed = false;
    }
}
