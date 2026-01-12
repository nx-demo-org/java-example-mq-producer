package com.example.producer.avro;

import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import java.io.ByteArrayOutputStream;

public final class AvroCodec {
  private AvroCodec() {}

  public static <T extends SpecificRecord> byte[] encode(T record) {
    try (var out = new ByteArrayOutputStream()) {
      var writer = new SpecificDatumWriter<T>(record.getSchema());
      var encoder = EncoderFactory.get().binaryEncoder(out, null);
      writer.write(record, encoder);
      encoder.flush();
      return out.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Failed to encode Avro record", e);
    }
  }
}
