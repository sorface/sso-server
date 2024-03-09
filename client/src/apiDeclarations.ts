import {ApiContractGet, ApiContractPost} from './types/apiContracts';

export type SignUpBody = Record<string, FormDataEntryValue>;

export const accountsApiDeclaration = {
    signup: (body: SignUpBody): ApiContractPost => ({
        method: 'POST',
        baseUrl: '/api/accounts/signup',
        body,
    }),
    signin: (body: FormData): ApiContractPost => ({
        method: 'POST',
        baseUrl: '/api/accounts/signin',
        body,
    }),
    current: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: '/api/accounts/current',
    })
};
