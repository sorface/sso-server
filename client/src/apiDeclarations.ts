import {ApiContractGet, ApiContractPost} from './types/apiContracts';

export type SignUpBody = Record<string, FormDataEntryValue>;

export interface ConfirmBody {
    token: string;
}

export const accountsApiDeclaration = {
    signup: (body: SignUpBody): ApiContractPost => ({
        method: 'POST',
        baseUrl: '/accounts/signup',
        body,
    }),
    current: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: '/api/accounts/current',
    }),
    confirm: (body: ConfirmBody): ApiContractPost => ({
        method: 'POST',
        baseUrl: '/api/account/confirm',
        body,
    }),
    getCurrentSession: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: '/api/sessions',
    }),
    logout: (): ApiContractPost => ({
        method: 'POST',
        baseUrl: '/api/accounts/logout',
        body: ''
    })
};
