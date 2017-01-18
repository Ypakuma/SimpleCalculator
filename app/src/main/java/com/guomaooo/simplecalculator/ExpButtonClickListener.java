package com.guomaooo.simplecalculator;

import android.view.View;

class ExpButtonClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        MainActivity activity = (MainActivity) v.getContext();

        // 未输入任何字符
        if (activity.getExpressionText().length() == 0) {
            // 之前没有结果，新输入
            if (activity.getResultText().length() == 0 ||
                    Double.isInfinite(Double.parseDouble(activity.getResultText().toString())) ||
                    Double.isNaN(Double.parseDouble(activity.getResultText().toString()))) {
                // 按键是负号
                if (v.getId() == R.id.button_min) {
                    activity.appendExpressionText("-");
                }
                return;
            } else if (activity.getResultText().length() != 0 &&
                    !Double.isInfinite(Double.parseDouble(activity.getResultText().toString())) &&
                    !Double.isNaN(Double.parseDouble(activity.getResultText().toString()))) {
                // 之前已有结果，不是新输入
                // 不是Infinity、NaN
                activity.setExpressionText(activity.getResultText());
            }

        }

        CharSequence expCharSequence = activity.getExpressionText();
        int expLen = expCharSequence.length();
        // 已输入其他运算符
        char lastChar = expCharSequence.charAt(expLen - 1);
        // 按键是负号
        if (v.getId() == R.id.button_min && "*/^".indexOf(lastChar) >= 0) {
            activity.appendExpressionText("-");
            return;
        }
        if (lastChar == '√') {
            return;
        }
        if ("+-*/^".indexOf(lastChar) >= 0) {
            activity.setExpressionText(expCharSequence.subSequence(0, expLen - 1));
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
