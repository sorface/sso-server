import {ApiContractGet, ApiContractPost} from './types/apiContracts';

export type SignUpBody = Record<string, FormDataEntryValue>;

export const accountsApiDeclaration = {
    signup: (body: SignUpBody): ApiContractPost => ({
        method: 'POST',
        baseUrl: '/api/accounts/signup',
        body,
    }),
    current: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: '/api/accounts/current',
    })
};
