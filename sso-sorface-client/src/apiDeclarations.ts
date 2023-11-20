import { ApiContractPost } from './types/apiContracts';

export type SignUpBody = Record<string, FormDataEntryValue>;

export const usersApiDeclaration = {
  registry: (body: SignUpBody): ApiContractPost => ({
    method: 'POST',
    baseUrl: '/api/users/registry',
    body,
  }),
};

export const signInApiDeclaration = {
  login: (body: FormData): ApiContractPost => ({
    method: 'POST',
    baseUrl: '/login',
    body,
  }),
};
