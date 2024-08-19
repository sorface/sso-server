import React, { ChangeEvent, FunctionComponent, useContext, useEffect, useState } from 'react';
import { Captions, IconNames } from "../../constants";
import { Icon } from '../../components/Icon/Icon';
import { AuthContext } from '../../context/AuthContext';
import { LogoutForm } from '../../components/LogoutForm/LogoutForm';
import { useApiMethodCsrf } from '../../hooks/useApiMethodCsrf';
import { accountsApiDeclaration, EditAccountBody } from '../../apiDeclarations';
import { Loader } from '../../components/Loader/Loader';
import { UserAvatar } from '../../components/UserAvatar/UserAvatar';

import './Account.css';
import {Account} from "../../types/account";

interface ProfileField {
    name: string;
    caption: string;
    value: string | undefined;
    editable: boolean;
}

export const AccountPage: FunctionComponent = () => {
    const { account, loadAccount } = useContext(AuthContext);
    const { fetchData, apiMethodState } = useApiMethodCsrf<unknown, EditAccountBody>(accountsApiDeclaration.edit);
    const { data, process: { error, loading } } = apiMethodState;
    const [editedFieldName, setEditedFieldName] = useState('');
    const [editedFieldValue, setEditedFieldValue] = useState('');

    const handleEditField = (fieldName: string, initialValue: string) => {
        setEditedFieldName(fieldName);
        setEditedFieldValue(initialValue);
    };

    const handleEditedFieldChangeValue = (e: ChangeEvent<HTMLInputElement>) => {
        setEditedFieldValue(e.target.value);
    };

    const handleEditedFieldCancel = () => {
        setEditedFieldName('');
    };

    useEffect(() => {
        if (!data) {
            return;
        }
        loadAccount();
    }, [data, loadAccount]);

    useEffect(() => {
        console.log('pre', apiMethodState.data);
        if (apiMethodState.data == null) {
            return;
        }
        console.log('post', apiMethodState.data);
        loadAccount();
    }, [apiMethodState.data, loadAccount]);

    const handleEditedFieldSave = () => {
        if (!account) {
            console.warn('Account is empty')
            return;
        }
        handleEditedFieldCancel();
        fetchData({
            id: account.id,
            [editedFieldName]: editedFieldValue,
        });
    };

    const getCaptionTextAvatar = (account: Account | null) : string | undefined => {
        if (account?.firstName && account?.lastName) {
            return `${account?.firstName[0].toUpperCase()} ${account?.lastName[0].toUpperCase()}`
        }

        return account?.nickname.charAt(0);
    }


    const fields: ProfileField[] = [
        {
            name: 'id',
            caption: Captions.Id,
            value: account?.id,
            editable: false,
        },
        {
            name: 'email',
            caption: Captions.Email,
            value: account?.email,
            editable: false,
        },
        {
            name: 'firstname',
            caption: Captions.FirstName,
            value: account?.firstName,
            editable: true,
        },
        {
            name: 'lastname',
            caption: Captions.LastName,
            value: account?.lastName,
            editable: true,
        },
    ];

    return (
        <div className='account-page'>
            <UserAvatar
                nickname={`${account?.nickname}`}
                caption={getCaptionTextAvatar(account)}
                src={account?.avatar}
            />
            {!!error && (<div>{Captions.Error}: {error}</div>)}
            {!!loading && (<div><Loader /></div>)}
            <table className="user-data-table">
                {fields.map(({ name, caption, value, editable }) => (
                    <tr key={name}>
                        <td className="bold left">{caption}</td>
                        {name !== editedFieldName ? (
                            <td className="right">
                                <div className="field-value">{value || Captions.Unknown}</div>
                                {editable && (
                                    <div className="field-action" onClick={() => handleEditField(name, value || '')}>
                                        <Icon name={IconNames.Create} />
                                    </div>
                                )}
                            </td>
                        ) : (
                            <td className="right">
                                <input className="field-value" type="text" value={editedFieldValue} onChange={handleEditedFieldChangeValue} />
                                <div className="field-action" onClick={handleEditedFieldSave}>
                                    <Icon name={IconNames.Checkmark} />
                                </div>
                                <div className="field-action" onClick={handleEditedFieldCancel}>
                                    <Icon name={IconNames.Close} />
                                </div>
                            </td>
                        )}
                    </tr>
                ))}
            </table>
            <LogoutForm />
        </div>
    );
};
