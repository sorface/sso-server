import {getCookie} from "./cookie";

export const X_CSRF_TOKEN_COOKIE_NAME = 'x_csrf_token';

export const X_CSRF_TOKEN_FORM_NAME = '_csrf';

const X_CSRF_TOKEN_HEADER = 'X-CSRF-TOKEN';

export const isPresentCsrfCookie = (): boolean => {
    return !!getCookie(X_CSRF_TOKEN_COOKIE_NAME);
}

export const setupCsrf = (name: String, targetHeaders: Headers): boolean => {
    const cookie = getCookie(name);

    if (cookie) {
        targetHeaders.set(X_CSRF_TOKEN_HEADER, cookie);

        return true;
    }

    return false;
}
