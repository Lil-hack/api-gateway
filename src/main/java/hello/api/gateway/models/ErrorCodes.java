package hello.api.gateway.models;

public enum ErrorCodes {
    ERROR_401("{\"error\":\"Ошибка авторизации oauth2\"}"),
    ERROR_503_USER("{\"error\":\"Внутренняя ошибка при работе сервера пользователей. Мы работаем над решением данной проблемы.\"}"),
    ERROR_503_STATISTIC("{\"error\":\"Внутренняя ошибка при работе сервера статистики. Мы работаем над решением данной проблемы.\"}"),
    ERROR_503_STATONLINE("{\"error\":\"Внутренняя ошибка при работе сервера статистики онлайн. Мы работаем над решением данной проблемы.\"}"),
    ERROR_503_USER_LOGIN("{\"error\":\"Внутренняя ошибка при работе сервера авторизации пользователей. Мы работаем над решением данной проблемы.\"}");


    private String error;

    ErrorCodes(String error) {
        this.error = error;
    }

    public String error() {
        return error;
    }
}
