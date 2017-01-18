package com.guomaooo.simplecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView expressionText;
    private TextView resultText;

    private ExpButtonClickListener expButtonClickListener = new ExpButtonClickListener();
    private DigitButtonClickListener digitButtonClickListener = new DigitButtonClickListener();


    public void setExpressionText(CharSequence text) {
        expressionText.setText(text);
    }

    public CharSequence getExpressionText() {
        return expressionText.getText();
    }

    public void appendExpressionText(String s) {
        expressionText.append(s);
    }

    public void setResultText(CharSequence text) {
        resultText.setText(text);
    }

    public CharSequence getResultText() {
        return resultText.getText();
    }

    private Stack<Double> numStack = new Stack<>();
    private Stack<Integer> operatorStack = new Stack<>();

    static final int OPERATION_ADD = 1;
    static final int OPERATION_MIN = 2;
    static final int OPERATION_MUL = 3;
    static final int OPERATION_DEV = 4;
    static final int OPERATION_POW = 5;
    static final int OPERATION_SQRT = 6;
    static final int OPERATION_NEG = 7;

    // 计算表达式栏结果
    // 每次按数字键时运行
    void calculate() {
        try {
            char[] expression = expressionText.getText().toString().toCharArray();

            StringBuilder numBuilder = new StringBuilder();

            for (char ch : expression) {
                if (Character.isDigit(ch) || ch == '.') { // 当前字符是数字
                    numBuilder.append(ch);
                } else if (ch == '√') { // 当前字符是单目运算符根号
                    if (numBuilder.length() != 0) {
                        numStack.push(Double.parseDouble(numBuilder.toString()));

                        operatorStack.push(OPERATION_MUL);
                        numBuilder.setLength(0);
                        numBuilder.trimToSize();
                    }
                    operatorStack.push(OPERATION_SQRT);
                } else if (ch == '-' && numBuilder.length() == 0) { // 当前字符是单目运算符负号
                    operatorStack.push(OPERATION_NEG);
                } else { // 当前字符是双目运算符
                    if (numBuilder.length() == 0) {
                        throw new ArrayStoreException("101");
                    }
                    numStack.push(Double.parseDouble(numBuilder.toString()));
                    numBuilder.setLength(0);
                    numBuilder.trimToSize();

                    if (!operatorStack.empty()) {
                        int innerPriority = getOperatorPriority(operatorStack.peek());
                        int outerPriority = getOperatorPriority(operatorCharToInt(ch));

                        while (innerPriority > outerPriority) {
                            stackCalculate();
                            if (operatorStack.empty()) {
                                break;
                            }
                            innerPriority = getOperatorPriority(operatorStack.peek());
                        }
                    }
                    operatorStack.push(operatorCharToInt(ch));
                }
            }

            // 将最后一个数压入栈
            if (numBuilder.length() == 0) {
                throw new ArrayStoreException("102");
            }
            numStack.push(Double.parseDouble(numBuilder.toString()));

            while (!operatorStack.empty()) {
                stackCalculate();
            }

            // 栈中最后一个数即结果
            // 设置结果栏
            if (!numStack.empty()) {
                double result = numStack.pop();
                // 判断结果是否为整数
                if (result % 1 == 0) {
                    resultText.setText(String.valueOf((int)result));
                }
                else {
                    resultText.setText(String.valueOf(result));
                }
            } else {
                throw new ArrayStoreException("103");
            }
        } catch (ArrayStoreException e) {
            Log.d(TAG, e.getMessage());
            Log.d(TAG, "error in calculate()");
            Log.d(TAG, "expression: " + expressionText.getText());
            Log.d(TAG, "result: " + resultText.getText());
            Toast.makeText(MainActivity.this, "ERROR 1 >.<\nClick \"C\" to clear", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d(TAG, "error in calculate()");
            Log.d(TAG, "expression: " + expressionText.getText());
            Log.d(TAG, "result: " + resultText.getText());
            Toast.makeText(MainActivity.this, "ERROR 0 >.<\nClick \"C\" to clear", Toast.LENGTH_SHORT).show();
        }
    }

    // char转化为int
    // 区分减号和负号
    private int operatorCharToInt(char operator) {
        switch (operator) {
            case '+':
                return OPERATION_ADD;
            case '-':
                return OPERATION_MIN;
            case '*':
                return OPERATION_MUL;
            case '/':
                return OPERATION_DEV;
            case '^':
                return OPERATION_POW;
            case '√':
                return OPERATION_SQRT;
            default:
                Toast.makeText(MainActivity.this, "ERROR 2 >.<\nClick \"C\" to clear", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "error in operatorCharToInt()");
                Log.d(TAG, "expression: " + expressionText.getText());
                Log.d(TAG, "result: " + resultText.getText());
                return 0;
        }
    }

    // 栈内单次计算
    private void stackCalculate() {
        int operator = operatorStack.pop();

        // 单目运算符
        double lastNum = numStack.pop();
        if (operator == OPERATION_SQRT) {
            numStack.push(Math.sqrt(lastNum));
            return;
        } else if (operator == OPERATION_NEG) {
            numStack.push(-lastNum);
            return;
        }

        // 双目运算符
        double firstNum = numStack.pop();
        switch (operator) {
            case OPERATION_ADD:
                numStack.push(firstNum + lastNum);
                break;
            case OPERATION_MIN:
                numStack.push(firstNum - lastNum);
                break;
            case OPERATION_MUL:
                numStack.push(firstNum * lastNum);
                break;
            case OPERATION_DEV:
                numStack.push(firstNum / lastNum);
                break;
            case OPERATION_POW:
                numStack.push(Math.pow(firstNum, lastNum));
                break;
            default:
                Toast.makeText(MainActivity.this, "ERROR 3 >.<\nClick \"C\" to clear", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "error in stackCalculate()");
                Log.d(TAG, "expression: " + expressionText.getText());
                Log.d(TAG, "result: " + resultText.getText());
                break;
        }
    }

    // 清空表达式栏、结果栏
    public void clearAll() {
        expressionText.setText("");
        resultText.setText("");
        expressionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, largeExpressionSizeSP);
    }

    // 获取算符优先级
    private int getOperatorPriority(int operator) {
        switch (operator) {
            case OPERATION_ADD:
            case OPERATION_MIN:
                return 1;
            case OPERATION_MUL:
            case OPERATION_DEV:
                return 2;
            case OPERATION_POW:
                return 3;
            case OPERATION_SQRT:
                return 4;
            case OPERATION_NEG:
                return 5;
            default:
                return 0;
        }
    }

    private int maxExpressionChar;

    private static final float largeExpressionSizeSP = 60;
    private static final float smallExpressionSizeSP = 40;
    private float nowExpressionSizeSP;

    // 当字符数过多或过少时，改变字体大小
    void changeExpressionTextSize() {
        if (expressionText.getText().length() >= maxExpressionChar && nowExpressionSizeSP == largeExpressionSizeSP) {
            expressionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallExpressionSizeSP);
        }
        if (expressionText.getText().length() <= maxExpressionChar && nowExpressionSizeSP == smallExpressionSizeSP) {
            expressionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, largeExpressionSizeSP);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTextView();
        initDigitButtons();
        initFunctionButtons();
        expressionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, largeExpressionSizeSP);

        nowExpressionSizeSP = largeExpressionSizeSP;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        maxExpressionChar = displayWidth / (int) (expressionText.getTextSize() * 0.75);
    }

    private void initTextView() {

        expressionText = (TextView) findViewById(R.id.expression_text);
        resultText = (TextView) findViewById(R.id.result_text);
    }

    private void initDigitButtons() {

        Button digitButton;

        digitButton = (Button) findViewById(R.id.button_0);
        digitButton.setOnClickListener(digitButtonClickListener);

        digitButton = (Button) findViewById(R.id.button_1);
        digitButton.setOnClickListener(digitButtonClickListener);

        digitButton = (Button) findViewById(R.id.button_2);
        digitButton.setOnClickListener(digitButtonClickListener);

        digitButton = (Button) findViewById(R.id.button_3);
        digitButton.setOnClickListener(digitButtonClickListener);

        digitButton = (Button) findViewById(R.id.button_4);
        digitButton.setOnClickListener(digitButtonClickListener);

        digitButton = (Button) findViewById(R.id.button_5);
        digitButton.setOnClickListener(digitButtonClickListener);

        digitButton = (Button) findViewById(R.id.button_6);
        digitButton.setOnClickListener(digitButtonClickListener);

        digitButton = (Button) findViewById(R.id.button_7);
        digitButton.setOnClickListener(digitButtonClickListener);

        digitButton = (Button) findViewById(R.id.button_8);
        digitButton.setOnClickListener(digitButtonClickListener);

        digitButton = (Button) findViewById(R.id.button_9);
        digitButton.setOnClickListener(digitButtonClickListener);

        digitButton = (Button) findViewById(R.id.button_point);
        digitButton.setOnClickListener(digitButtonClickListener);
    }

    private void initFunctionButtons() {

        Button functionButton;

        functionButton = (Button) findViewById(R.id.button_add);
        functionButton.setOnClickListener(expButtonClickListener);

        functionButton = (Button) findViewById(R.id.button_min);
        functionButton.setOnClickListener(expButtonClickListener);

        functionButton = (Button) findViewById(R.id.button_mul);
        functionButton.setOnClickListener(expButtonClickListener);

        functionButton = (Button) findViewById(R.id.button_dev);
        functionButton.setOnClickListener(expButtonClickListener);

        functionButton = (Button) findViewById(R.id.button_pow);
        functionButton.setOnClickListener(expButtonClickListener);

        functionButton = (Button) findViewById(R.id.button_sqrt);
        functionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionText.append("√");
                // 只有结果时，按下根号，自动计算结果的根号
                if (expressionText.length() == 1){
                    expressionText.append(resultText.getText());
                    calculate();
                }
            }
        });

        functionButton = (Button) findViewById(R.id.button_equal);
        functionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressionText.setText("");
            }
        });

        functionButton = (Button) findViewById(R.id.button_clear);
        functionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });

        functionButton = (Button) findViewById(R.id.button_del);
        functionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除一个字符
                StringBuilder exp = new StringBuilder(expressionText.getText());
                if (exp.length() == 0) {
                    return;
                }
                exp.setLength(exp.length() - 1);
                expressionText.setText(exp);

                // 计算剩余表达式
                if (exp.length() > 0) {
                    // 表达式栏最后一个字符是数字
                    if (Character.isDigit(exp.charAt(expressionText.length() - 1)) || exp.charAt(expressionText.length() - 1) == '.') {
                        calculate();
                    } else {
                        resultText.setText("");
                    }
                }
            }
        });
    }

}
