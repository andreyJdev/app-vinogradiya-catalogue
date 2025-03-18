package ru.vinogradiya.utils.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Generated;

@Schema(description = "Сообщение об ошибке")
public class ApiError {

    @Schema(description = "URL при обработку которого возникла ошибка")
    private String url;

    @Schema(description = "HTTP статус ошибки")
    private Integer status;

    @Schema(description = "HTTP сообщение ошибки")
    private String error;

    @Schema(description = "Тип ошибки")
    private String type;

    @Schema(description = "Детали ошибки")
    private Object details;

    @Generated
    public ApiError() {
    }

    @Generated
    public String getUrl() {
        return url;
    }

    @Generated
    public Integer getStatus() {
        return status;
    }

    @Generated
    public String getError() {
        return error;
    }

    @Generated
    public String getType() {
        return type;
    }

    @Generated
    public Object getDetails() {
        return details;
    }

    @Generated
    public void setUrl(String url) {
        this.url = url;
    }

    @Generated
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Generated
    public void setError(String error) {
        this.error = error;
    }

    @Generated
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public void setDetails(Object details) {
        this.details = details;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ApiError)) {
            return false;
        } else {
            ApiError other = (ApiError) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label1:
                {
                    Object this$status = this.getStatus();
                    Object other$status = other.getStatus();
                    if (this$status == null) {
                        if (other$status == null) {
                            break label1;
                        }
                    } else if (this$status.equals(other$status)) {
                        break label1;
                    }

                    return false;
                }

                Object this$url = this.getUrl();
                Object other$url = other.getUrl();
                if (this$url == null) {
                    if (other$url != null) {
                        return false;
                    }
                } else if (!this$url.equals(other$url)) {
                    return false;
                }

                label2:
                {
                    Object this$error = this.getError();
                    Object other$error = other.getError();
                    if (this$error == null) {
                        if (other$error == null) {
                            break label2;
                        }
                    } else if (this$error.equals(other$error)) {
                        break label2;
                    }

                    return false;
                }

                Object this$type = this.getType();
                Object other$type = other.getType();
                if (this$type == null) {
                    if (other$type != null) {
                        return false;
                    }
                } else if (!this$type.equals(other$type)) {
                    return false;
                }

                Object this$details = this.getDetails();
                Object other$details = other.getDetails();
                if (this$details == null) {
                    if (other$details == null) {
                        return true;
                    }
                } else if (this$details.equals(other$details)) {
                    return true;
                }

                return false;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ApiError;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        Object $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        Object $error = this.getError();
        result = result * 59 + ($error == null ? 43 : $error.hashCode());
        Object $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        Object $details = this.getDetails();
        result = result * 59 + ($details == null ? 43 : $details.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getUrl();
        return "ApiError(url=" + var10000 + ", status=" + this.getStatus() + ", error=" + this.getError() + ", type=" + this.getType() + ", details=" + this.getDetails() + ")";
    }
}