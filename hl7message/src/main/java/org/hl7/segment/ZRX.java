package org.hl7.segment;

import static org.hl7.segment.ISegment.PIPE;
import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Objects;

/**
 * ZRX is a custom segment for some specific interfaces.
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class ZRX {
  public static final String segmentId = "ZRX";
  @Builder.Default
  private final String zrx_1_dispenseCode = "";
  @Builder.Default
  private final String zrx_3_patientChargeCode = "";
  @Builder.Default
  private final String zrx_4_retailPharmacyOriginalDate = "";

  /**
   * Converts String to object that implements ISegment.
   */
  public static ZRX fromString(String segment) {
    String[] fields = segment.split("\\".concat(PIPE), -1);

    if (!Objects.equals(fields[0], segmentId)) {
      throw new IllegalArgumentException("Invalid message segment [" + fields[0] + "]. Expected [" + segmentId + "]");
    }

    ZRX.ZRXBuilder zrx = ZRX.builder();
    getField(fields, 1).ifPresent(zrx::zrx_1_dispenseCode);
    getField(fields, 3).ifPresent(zrx::zrx_3_patientChargeCode);
    getField(fields, 4).ifPresent(zrx::zrx_4_retailPharmacyOriginalDate);
    return zrx.build();
  }

  public String print() {
    return segmentId + "|" + zrx_1_dispenseCode + "||" + zrx_3_patientChargeCode + "|" + zrx_4_retailPharmacyOriginalDate
        + "||||||||";
  }
}
