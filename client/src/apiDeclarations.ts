import {ApiContractDelete, ApiContractGet, ApiContractPatch, ApiContractPost} from './types/apiContracts';

export type SignUpBody = Record<string, FormDataEntryValue>;

export interface ConfirmBody {
    token: string;
}

export const csrfApiDeclaration = {
    csrf: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: '/csrf'
    })
}

export const appsApiDeclaration = {
    getMyApps: (): ApiContractGet => ({
        method: "GET",
        baseUrl: "/api/applications"
    }),
    getById: (id: string): ApiContractGet => ({
        method: "GET",
        baseUrl: `/api/applications/${id}`
    }),
    deleteById: (id: string): ApiContractDelete => ({
        method: "DELETE",
        baseUrl: `/api/applications`,
        body: {
            id
        }
    })
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
    edit: (): ApiContractPatch => ({
        method: 'PATCH',
        baseUrl: `/api/accounts/:id`,
        body: {
            firstname: '',
            lastname: ''
        }
    }),
    logout: (): ApiContractPost => ({
        method: 'POST',
        baseUrl: '/api/accounts/logout',
        body: ''
    })
};
