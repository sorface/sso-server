import {ApiContractGet, ApiContractPost} from './types/apiContracts';

export type SignUpBody = Record<string, FormDataEntryValue>;

export const usersApiDeclaration = {
    registry: (body: SignUpBody): ApiContractPost => ({
        method: 'POST',
        baseUrl: '/api/users/registry',
        body,
    }),
    getCurrentUser: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: '/api/profiles',
    })
};

export const signInApiDeclaration = {
    login: (body: FormData): ApiContractPost => ({
        method: 'POST',
        baseUrl: '/api/login',
        body,
    }),
};

export const getCurrentUser = {};
