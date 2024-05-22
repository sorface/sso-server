import {ApiContractDelete, ApiContractGet, ApiContractPatch, ApiContractPost, ApiEndpoint} from './types/apiContracts';

export type SignUpBody = Record<string, FormDataEntryValue>;

export interface ConfirmBody {
    token: string;
}

export const csrfApiDeclaration = {
    csrf: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: ApiEndpoint.Csrf,
    })
}

export const appsApiDeclaration = {
    getMyApps: (): ApiContractGet => ({
        method: "GET",
        baseUrl: ApiEndpoint.Apps,
    }),
    getById: (id: string): ApiContractGet => ({
        method: "GET",
        baseUrl: ApiEndpoint.GetAppById.replace(':id', id) as ApiEndpoint,
    }),
    deleteById: (id: string): ApiContractDelete => ({
        method: "DELETE",
        baseUrl: ApiEndpoint.Apps,
        body: {
            id
        }
    })
}

export const accountsApiDeclaration = {
    signup: (body: SignUpBody): ApiContractPost => ({
        method: 'POST',
        baseUrl: ApiEndpoint.AccountsSignup,
        body,
    }),
    current: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: ApiEndpoint.AccountsCurrent,
    }),
    confirm: (body: ConfirmBody): ApiContractPost => ({
        method: 'POST',
        baseUrl: ApiEndpoint.AccountsConfirm,
        body,
    }),
    getCurrentSession: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: ApiEndpoint.CurrentSession,
    }),
    edit: (): ApiContractPatch => ({
        method: 'PATCH',
        baseUrl: ApiEndpoint.AccountsEdit,
        body: {
            firstname: '',
            lastname: ''
        }
    }),
    logout: (): ApiContractPost => ({
        method: 'POST',
        baseUrl: ApiEndpoint.AccountsLogout,
        body: ''
    })
};
