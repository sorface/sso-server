import { ApiContractPost } from './types/apiContracts';

export type SignInBody = Record<string, FormDataEntryValue>;

export const usersApiDeclaration = {
  registry: (body: SignInBody): ApiContractPost => ({
    method: 'POST',
    baseUrl: '/users/registry',
    body,
  }),
};

export const loginApiDeclaration = {
  login: (body: FormData): ApiContractPost => ({
    method: 'POST',
    baseUrl: '/login',
    body,
  }),
};
