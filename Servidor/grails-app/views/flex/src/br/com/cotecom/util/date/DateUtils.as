package br.com.cotecom.util.date {

public class DateUtils {

    public static function parseDate(date:String):Date {
        var year:String;
        var month:String;
        var day:String;
        var hour:String;
        var minutes:String;
        var newDate:Date;

        year = date.substr(6, 2);
        year = "20" + year;
        month = date.substr(3, 2);
        day = date.substr(0, 2);
        hour = date.substr(9,2);
        minutes = date.substr(12,2);
        newDate = new Date(Number(year),Number(month),Number(day),Number(hour),Number(minutes));
        return(newDate);
    }
}
}
