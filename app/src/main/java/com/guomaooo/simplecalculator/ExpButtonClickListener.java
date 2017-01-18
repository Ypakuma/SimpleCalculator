package com.guomaooo.simplecalculator;

import android.view.View;

class ExpButtonClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        MainActivity activity = (MainActivity) v.getContext();

        // 未输入任何字符
        if (activity.getExpressionText().length() == 0) {
            // 之前没有结果，或结果不是可运算的数新输入
            if (activity.getResultText().length() == 0 ||
                    Double.isInfinite(Double.parseDouble(activity.getResultText().toString())) ||
                    Double.isNaN(Double.parseDouble(activity.getResultText().toString()))) {
                // 按键当成负号
                if (v.getId() == R.id.button_min) {
                    activity.appendExpressionText("-");
                }
                // 直接返回
                return;
            } else {
                // 之前已有结果，不是Infinity、NaN
                // 将结果当成已输入字符
                activity.setExpressionText(activity.getResultText());
            }
        }

        CharSequence expCharSequence = activity.getExpressionText();
        int expLen = expCharSequence.length();
        // 已输入其他运算符
        char lastChar = expCharSequence.charAt(expLen - 1);

        // 根号后面只能跟数，不能跟运算符
        if (lastChar == '√') {
            return;
        }

        if (lastChar == '+') {
            // 已输入加号，直接替换
            activity.setExpressionText(expCharSequence.subSequence(0, expLen - 1));
        } else if (lastChar == '*' || lastChar == '/' || lastChar == '^') {
            // 减号按键当成负号，显示后直接返回
            if (v.getId() == R.id.button_min) {
                activity.appendExpressionText("-");
                return;
            }
            // 已输入其他运算符，直接替换
            activity.setExpressionText(expCharSequence.subSequence(0, expLen - 1));
        } else if (lastChar == '-') {
            // 输入栏只有负号，直接返回
            if (expLen == 1) {
                return;
            } else {
                // 通过倒数第二个字符是不是数，判断减号/负号
                char lastTwoChar = expCharSequence.charAt(expLen - 2);
                if (Character.isDigit(lastTwoChar) || lastTwoChar == '.') {
                    // 减号
                    activity.setExpressionText(expCharSequence.subSequence(0, expLen - 1));
                } else {
                    // 负号
                    return;
                }
            }
        }


        switch (v.getId()) {
            case R.id.button_add:
                activity.appendExpressionText("+");
                break;
            case R.id.button_min:
                activity.appendExpressionText("-");
                break;
            case R.id.button_mul:
                activity.appendExpressionText("*");
                break;
            case R.id.button_dev:
                activity.appendExpressionText("/");
                break;
            case R.id.button_pow:
                activity.appendExpressionText("^");
                break;
            default:
                break;
        }

        activity.changeExpressionTextSize();
    }
}
