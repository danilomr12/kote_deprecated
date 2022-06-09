package br.com.cotecom.model.utils {
import mx.validators.ValidationResult;
import mx.validators.Validator;

public class CnpjValidator extends Validator
{
    public function CnpjValidator()
    {
        super();
    }

    override protected function doValidation(value:Object):Array
    {
        var results:Array = super.doValidation(value.text);
        var a:Array = new Array();
        var b:Number = new Number;
        var i:Number;
        var x:Number;
        var y:Number;
        var c:Array = [6,5,4,3,2,9,8,7,6,5,4,3,2];
        var CNPJ:String = value.text;

        // Retira os dígitos formatadores de CNPJ '.' e '-', caso existam.
        CNPJ.replace(".", "");
        CNPJ.replace("-", "");
        CNPJ.replace("/", "");

        //verifica CNPJs manjados

        switch (CNPJ)
        {
            case '0':
            case '00':
            case '000':
            case '0000':
            case '00000':
            case '000000':
            case '0000000':
            case '00000000':
            case '000000000':
            case '0000000000':
            case '00000000000':
            case '11111111111':
            case '22222222222':
            case '33333333333':
            case '44444444444':
            case '55555555555':
            case '66666666666':
            case '77777777777':
            case '88888888888':
            case '99999999999':
                results.push(new ValidationResult(true, null, "Erro","Número do CNPJ inválido!"));
                return results;
                break;
        }

        for (i=0; i < 12; i++)
        {
            a[i] = CNPJ.charAt(i);
            b += a[i] * c[i+1];
        }

        if ((x = b % 11) < 2) { a[12] = 0 } else { a[12] = 11-x }

        b = 0;

        for (y=0; y < 13; y++)
        {
            b += (a[y] * c[y]);
        }

        if ((x = b % 11) < 2) { a[13] = 0; } else { a[13] = 11-x; }

        if ((CNPJ.charAt(12) != a[12]) || (CNPJ.charAt(13) != a[13]))
        {
            results.push(new ValidationResult(true, null, "Erro","Número do CNPJ inválido!"));
        }

        return results;
    }

    override protected function getValueFromSource():Object
    {
        var value:Object = {};
        value.text = super.getValueFromSource();
        return  value;
    }
}
}