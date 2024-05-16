export enum AccountRole {
  ADMIN = 'ADMIN',
}

export interface Account {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  avatar: string;
  roles: AccountRole[];
}
