package com.guomaooo.simplecalculator;

import android.view.View;

class DigitButtonClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        MainActivity activity = (MainActivity) v.getContext();
        if (activity.getExpressionText().length() == 0) {
            activity.setResultText("");
        }
        String ch;
        switch (v.getId()) {
            case R.id.button_0:
                ch = "0";
                break;
            case R.id.button_1:
                ch = "1";
                break;
            case R.id.button_2:
                ch = "2";
                break;
            case R.id.button_3:
                ch = "3";
                break;
            case R.id.button_4:
                ch = "4";
                break;
            case R.id.button_5:
                ch = "5";
                break;
            case R.id.button_6:
                ch = "6";
                break;
            case R.id.button_7:
                ch = "7";
                break;
            case R.id.button_8:
                ch = "8";
                break;
            case R.id.button_9:
                ch = "9";
                break;
            case R.id.button_point:
                ch = ".";
                break;
            default:
                ch = "";
                break;
        }
        activity.appendExpressionText(ch);
        activity.calculate();

        activity.changeExpressionTextSize();
    }
}