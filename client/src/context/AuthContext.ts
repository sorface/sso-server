import React from 'react';
import { Account } from '../types/account';

export const AuthContext = React.createContext<Account | null>(null);
