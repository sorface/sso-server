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
};

export interface CreateAppBody {
    name: string;
    redirectionUrls: string;
}

export const appsApiDeclaration = {
    getMyApps: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: ApiEndpoint.Apps,
    }),
    getById: (id: string): ApiContractGet => ({
        method: 'GET',
        baseUrl: ApiEndpoint.GetAppById.replace(':id', id) as ApiEndpoint,
    }),
    deleteById: (id: string): ApiContractDelete => ({
        method: 'DELETE',
        baseUrl: ApiEndpoint.Apps,
        body: {
            id
        }
    }),
    create: (body: CreateAppBody): ApiContractPost => ({
        method: 'POST',
        baseUrl: ApiEndpoint.Apps,
        body,
    }),
};

export interface EditAccountBody {
    id: string;
    firstName?: string;
    lastName?: string;
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
    edit: (body: EditAccountBody): ApiContractPatch => ({
        method: 'PATCH',
        baseUrl: ApiEndpoint.AccountsEdit.replace(':id', body.id) as ApiEndpoint,
        body: {
            ...body,
            id: undefined,   
        },
    }),
    logout: (): ApiContractPost => ({
        method: 'POST',
        baseUrl: ApiEndpoint.AccountsLogout,
        body: ''
    })
};
