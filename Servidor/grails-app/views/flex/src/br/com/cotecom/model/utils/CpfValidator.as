package br.com.cotecom.model.utils {

import mx.validators.ValidationResult;
import mx.validators.Validator;

public class CpfValidator extends Validator
{
    public function CpfValidator()
    {
        super();
    }

    override protected function doValidation(value:Object):Array
    {
        var results:Array = super.doValidation(value.text);
        var digito:Array = new Array(); // array para os dígitos do CPF.
        var aux:Number= 0;
        var posicao:Number;
        var i:Number;
        var soma:Number;
        var dv:Number;
        var dvInformado:Number;
        var CPF:String = value.text;

        // Retira os dígitos formatadores de CPF '.' e '-', caso existam.
        CPF.replace(".", "");
        CPF.replace("-", "");

        //verifica CPFs manjados
        switch (CPF)
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
                results.push(new ValidationResult(true, null, "Erro","Número do CPF inválido!"));
                return results;
                break;
        }

        // Início da validação do CPF.
        /* Retira do número informado os dois últimos dígitos */

        dvInformado = parseInt(CPF.substr(9,2));

        /* Desmembra o número do CPF no array digito */
        for (i=0; i<= 8; i++)
        {
            digito[i] = CPF.substr(i,1);
        }

        /* Calcula o valor do 10o. digito de verificação */
        posicao = 10;
        soma = 0;

        for (i=0; i <= 8; i++)
        {
            soma = soma + digito[i] * posicao;
            posicao--;
        }

        digito[9] = soma % 11;

        if (digito[9] < 2)
        {
            digito[9] = 0;
        } else {
            digito[9] = 11 - digito[9];
        }

        /* Calcula o valor do 11o. digito de verificação */
        posicao = 11;
        soma = 0;

        for (i=0; i <= 9; i++)
        {
            soma = soma + digito[i] * posicao;
            posicao--;
        }

        digito[10] = soma % 11;

        if (digito[10] < 2)
        {
            digito[10] = 0;
        } else {
            digito[10] = 11 - digito[10];
        }

        dv = digito[9] * 10 + digito[10];

        /* Verifica se o DV calculado é igual ao informado */
        if(dv != dvInformado)
        {
            results.push(new ValidationResult(true, null, "Erro","Número do CPF inválido!"));
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