package br.com.cotecom.view.util {

import mx.formatters.CurrencyFormatter;

public class DinheiroUtil {

    public function DinheiroUtil() {}

    public static function formatAsReal(valor:Object, precisao:int = 2):String{
        var currencyFormatter:CurrencyFormatter = new CurrencyFormatter();
        currencyFormatter.precision=precisao.toString();
        currencyFormatter.currencySymbol="R$ ";
        currencyFormatter.decimalSeparatorTo=",";
        currencyFormatter.decimalSeparatorFrom=",";
        currencyFormatter.thousandsSeparatorFrom=".";
        currencyFormatter.thousandsSeparatorTo=".";
        return currencyFormatter.format(valor);
    }

}
}