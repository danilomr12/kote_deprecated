<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Novo pedido</title>
</head>
<body>
<g:if test="${mailTemplate}">
    <p>
        <font color="#999999" face="Lucida Sans Unicode, Lucida Grande, sans-serif" size="-2">
            Você está recebendo esse email porque uma ordem de compra foi gerada para você no KOTE® pelo cliente <b>${nomeFantasiaCliente}</b>.
            <a href="${url}">Está com problemas para ler esse email? Veja no seu navegador</a>
        </font>
    </p>
</g:if>
<table width="650" border="1px" cellpadding="43" cellspacing="0" bordercolor="#CCCCCC" bordercolordark="#CCCCCC" bordercolorlight="#CCCCCC" bgcolor="#E5E5E5">
    <tr>
        <td>

            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td><a href="http://www.kote.com.br"><img src="https://s3-sa-east-1.amazonaws.com/www.kote.com.br/mail/logo.jpg" alt="Kote" width="63" height="49" border="0" /></a>
                        <br />
                        <br />
                    </td>
                </tr>
                <tr>
                    <td width="554" height="103" BACKGROUND="https://s3-sa-east-1.amazonaws.com/www.kote.com.br/mail/fundo1.jpg" >
                    </td>
                </tr>
                <tr>
                    <td width="554" BACKGROUND="https://s3-sa-east-1.amazonaws.com/www.kote.com.br/mail/fundo2.jpg" >

                        <table border="0" cellspacing="0" cellpadding="30">
                            <tr>
                                <td>

                                    <font color="#7F8185" face="Lucida Sans Unicode, Lucida Grande, sans-serif">Novo Pedido</font>
                                    <br />
                                    <br />
                                    <font size="-1" color="#999999" face="Lucida Sans Unicode, Lucida Grande, sans-serif">
                                        Olá ${nomeRepresentante},
                                        <p>O comprador <b>${nomeComprador}</b> da empresa <b>${nomeFantasiaCliente}</b>
                                            lhe enviou uma nova ordem de compra. Para melhorar o funcionamento do sistema,
                                            agora é necessário acessar seu pedido pelo sistema on-line pelo endereço
                                            <a href="http://app.kote.com.br">http://app.kote.com.br</a>. Caso não visualize o link,
                                        copie e cole o endereço na barra de endereços de seu navegador.
                                            <br>Caso tenha alguma dúvida você encontra informações
                                        sobre o sistema logo abaixo ou entre em contato conosco em <a href="mailto:equipe@kote.com.br">
                                            <font color="#E54B46"><u>equipe@kote.com.br</u></font></a> que o responderemos com prazer.</p>

                                    </font>

                                    <g:render template="/mail/template/footer_sobre_o_kote" />
                                </td>
                            </tr>
                        </table>

                    </td>
                </tr>
                <tr>
                    <td width="554" height="21" BACKGROUND="https://s3-sa-east-1.amazonaws.com/www.kote.com.br/mail/fundo3.jpg" ></td>
                </tr>
            </table>

            <g:render template="/mail/template/mail_footer_descadastrar" model="[emailComprador: emailComprador]" />

        </td>
    </tr>
</table>

</body>
</html>