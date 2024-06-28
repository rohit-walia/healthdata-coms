package org.hl7;

import org.hl7.common.MessageEvent;
import org.hl7.utils.Hl7DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class Hl7ConverterTest {

  @SuppressWarnings("checkstyle:lineLength")
  @Test
  void convertNewToDispense() {
    var dateTime = Hl7DateUtils.formatToHl7(LocalDateTime.now());

    String hl7StringNew = """
        MSH|^~\\&|||AB|LOCATION1|20240531000000||RDS^O13^RDS_O13|4154345958|P|2.5||||||ASCII|||
        PID|1|775908|08PAT||lastname^firstname^^^^||19690531000000|M|||||||||||||||||||||||||||||||
        ORC|NW||5288240975||||1^QHS&1200,1300^1^20240601111958^20240607111958^0||20240604100958|||1234567890^MedProFirstName^MedProLastName||||||||||||||||||
        RXO|Mirtazapine 7.5MG TAB|||||||||||||||||||||||||||
        RXE||69618001001^Mirtazapine 7.5MG TAB^||||TABS|^instructions||||||||58902||||||||||||F33.9^Depression^ICD10|||||||||||||||||
        TQ1|1|1^TAB|QHS|1200-1300|||20240607111958||P||Take 1 tablet my mouth every day for Depression|A||
        RXR|27^by mouth|||||
        RXD|1|69618001001^Mirtazapine 7.5MG TAB|20240607111040||||||||||||||||||||||||||||||
        """;

    String hl7StringDispense = """
        MSH|^~\\&|||AB|LOCATION1|%s||RDS^O13^RDS_O13|4154345958|P|2.5||||||ASCII|||
        PID|1|775908|08PAT||lastname^firstname^^^^||19690531000000|M|||||||||||||||||||||||||||||||
        ORC|RE||5288240975||||1^QHS&1200,1300^1^20240601111958^20240607111958^0||20240604100958|||1234567890^MedProFirstName^MedProLastName||||||||||||||||||
        RXO|Mirtazapine 7.5MG TAB|||||||||||||||||||||||||||
        RXE||69618001001^Mirtazapine 7.5MG TAB^||||TABS|^instructions||||||||58902||||||||||||F33.9^Depression^ICD10|||||||||||||||||
        TQ1|1|1^TAB|QHS|1200-1300|||20240607111958||P||Take 1 tablet my mouth every day for Depression|A||
        RXR|27^by mouth|||||
        RXD|1|69618001001^Mirtazapine 7.5MG TAB|20240607111040||||||||||||||||||||||||||||||
        """.formatted(dateTime)
        .trim()
        .replace("\r\n", "\n")
        .replace('\r', '\n');

    Hl7Message newHl7Message = Hl7Message.fromString(hl7StringNew);
    Hl7Message dispenseHl7Message = Hl7Converter.convert(newHl7Message, MessageEvent.ORDER_DISPENSE);

    // sync on dynamic values
    dispenseHl7Message.getMsh().setMsh_7_messageDatetime(dateTime);

    Assertions.assertEquals(hl7StringDispense, dispenseHl7Message.printMessage()
        .replace("\r\n", "\n")
        .replace('\r', '\n'));

  }
}