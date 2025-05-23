package ru.abramov.practicum.pay.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.annotation.Generated;

/**
 * PaymentRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-08T15:44:49.285739+03:00[Europe/Moscow]", comments = "Generator version: 7.12.0")
public class PaymentRequest {

  @NotNull(message = "Amount is required")
  @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
  private BigDecimal amount;

  @NotNull(message = "UserId is required")
  private String userId;

  public PaymentRequest amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public PaymentRequest userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public BigDecimal getAmount() {
    return amount;
  }

  /**
   * Get userId
   * @return userId
   */

  @Schema(name = "user_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("user_id")
  public String getUserId() {
    return userId;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }


  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentRequest paymentRequest = (PaymentRequest) o;
    return Objects.equals(this.amount, paymentRequest.amount) && Objects.equals(this.userId, paymentRequest.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, userId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentRequest {\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

