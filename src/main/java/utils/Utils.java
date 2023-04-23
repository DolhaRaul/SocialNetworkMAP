package utils;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Utils
{
    public static <T> Stream<T> toStream(Iterable<T> iterable)
    {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * @param data-Data obiectului de tip LocalDateTime
     * @param timp-Timpul obiectului de timp LocalDateTime
     * @return O zi calendaristica pentru care stim: ziua, luna, anul, ora si minutul
     */
    public static LocalDateTime creeazaDataTImp(Date data, Time timp)
    {
        LocalDateTime now = LocalDateTime.now();///DOAR asa putem creea obiecte LocalDateTime
        Calendar calendar = Calendar.getInstance();///o instanta de tip calendar(SINGLETON)
        ///asta o folosim ca sa obtinem data si timpul, pt ca obiectele Date si Time au metode
        ///dubioase "deprecated" de obtinere a acestor valori
        calendar.setTime(data);///ca sa obtinem detalii legate de data
        LocalDateTime data_trimitere = now.withDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH)).
                withMonth(calendar.get(Calendar.MONTH)).withYear(calendar.get(Calendar.YEAR));
        calendar.setTime(timp);///ca sa obtinem detalii legate de timp
        data_trimitere.withHour(calendar.get(Calendar.HOUR_OF_DAY)).
                withMinute(calendar.get(Calendar.MINUTE));
        return data_trimitere;
    }
}
