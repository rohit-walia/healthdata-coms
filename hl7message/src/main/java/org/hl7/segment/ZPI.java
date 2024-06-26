package org.hl7.segment;

import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.RandomStringUtils;
import org.hl7.segment.component.CodedElement;

/**
 * ZPI is a custom segment for some specific interfaces. Framework is a common user of this segment.
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class ZPI implements ISegment {
  public static final String segmentId = "ZPI";
  @Builder.Default
  private final String zpi_11_timesPerDay = "";
  @Builder.Default
  private final String zpi_12_prescribedDate = "";
  @Builder.Default
  private final String zpi_17_dispensePartialStatus = "";
  @Builder.Default
  private final CodedElement zpi_23_orderRequestId = CodedElement.builder().build();
  @Builder.Default
  private final String zpi_24_isPrn = "";
  @Builder.Default
  private final String zpi_25_linkedReorderNumber = "";
  @Builder.Default
  private final String zpi_30_explicitTime = "";
  @Builder.Default
  private final String zpi_33_startDate = "";
  @Builder.Default
  private final String zpi_34_rxNumber = RandomStringUtils.randomNumeric(5);

  /**
   * Converts String to object that implements ISegment.
   */
  public static ZPI fromString(String segment) {
    String[] fields = segment.split("\\".concat(PIPE), -1);

    if (!fields[0].equals(segmentId)) {
      throw new IllegalArgumentException("Invalid message segment [" + fields[0] + "]. Expected [" + segmentId + "]");
    }

    ZPIBuilder zpi = ZPI.builder();
    getField(fields, 11).ifPresent(zpi::zpi_11_timesPerDay);
    getField(fields, 12).ifPresent(zpi::zpi_12_prescribedDate);
    getField(fields, 17).ifPresent(zpi::zpi_17_dispensePartialStatus);
    getField(fields, 23).map(CodedElement::fromString).ifPresent(zpi::zpi_23_orderRequestId);
    getField(fields, 24).ifPresent(zpi::zpi_24_isPrn);
    getField(fields, 25).ifPresent(zpi::zpi_25_linkedReorderNumber);
    getField(fields, 30).ifPresent(zpi::zpi_30_explicitTime);
    getField(fields, 33).ifPresent(zpi::zpi_33_startDate);
    getField(fields, 34).ifPresent(zpi::zpi_34_rxNumber);
    return zpi.build();
  }

  @Override
  public String print() {
    return segmentId + "|||||||||||" + zpi_11_timesPerDay + "|" + zpi_12_prescribedDate + "|||||"
        + zpi_17_dispensePartialStatus + "||||||" + zpi_23_orderRequestId.print() + "|" + zpi_24_isPrn + "|"
        + zpi_25_linkedReorderNumber + "|||||" + zpi_30_explicitTime + "|||" + zpi_33_startDate + "|" + zpi_34_rxNumber;
  }
}
