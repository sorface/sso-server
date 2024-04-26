import {getCookie} from "./cookie";

export const X_CSRF_TOKEN_COOKIE_NAME = 'x_csrf_token';

export const X_CSRF_TOKEN_FORM_NAME = '_csrf';

export const X_CSRF_TOKEN_HEADER = 'X-CSRF-TOKEN';

export interface CsrfToken {

    parameterName: string;

    token: string;

    headerName: string;

}

export const isPresentCsrfCookie = (): boolean => {
    return !!getCookie(X_CSRF_TOKEN_COOKIE_NAME);
}

export const setupCsrf = (name: String, targetHeaders: Headers): boolean => {
    const cookie = getCookie(name);

    if (cookie) {
        const csrfToken = maskCsrfToken(cookie);

        if (csrfToken) {
            targetHeaders.set(X_CSRF_TOKEN_HEADER, csrfToken);
        }

        return true;
    }

    return false;
}

export const xorCsrfCookie = (cookieName: string) => {
    return maskCsrfToken(getCookie(cookieName) as string)
}

const maskCsrfToken = (token: string) => {
    if (!token) {
        return null;
    }
    const tokenBytes = [];
    for (let i = 0; i < token.length; i++) {
        tokenBytes.push(token.charCodeAt(i));
    }

    const randomBytes = new Uint8Array(tokenBytes.length);
    for (let i = 0; i < randomBytes.length; i++) {
        randomBytes[i] = Math.floor(Math.random() * 256);
    }

    const xoredBytes: Uint8Array | null = xorCsrf(randomBytes, tokenBytes);
    const combinedBytes: Uint8Array = new Uint8Array(tokenBytes.length + randomBytes.length);
    combinedBytes.set(randomBytes);
    combinedBytes.set(xoredBytes!, randomBytes.length);

    // @ts-ignore
    return btoa(String.fromCharCode.apply(null, combinedBytes))
        .replace(/\+/g, '-')
        .replace(/\//g, '_')
        .replace(/=+$/, '');
};

const xorCsrf = (randomBytes: Uint8Array, csrfBytes: number[]): Uint8Array | null => {
    if (csrfBytes.length < randomBytes.length) {
        return null;
    }
    const len = Math.min(randomBytes.length, csrfBytes.length);
    const xoredCsrf = new Uint8Array(len);
    xoredCsrf.set(csrfBytes.slice(0, len));
    for (let i = 0; i < len; i++) {
        xoredCsrf[i] ^= randomBytes[i];
    }
    return xoredCsrf;
};