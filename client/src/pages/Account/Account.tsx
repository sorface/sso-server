import { ChangeEvent, FunctionComponent, useEffect, useState } from 'react';
import { useApiMethod } from '../../hooks/useApiMethod';
import { accountsApiDeclaration } from '../../apiDeclarations';
import defaultAvatarImage from './img/anonymus_avatar.png';
import { useLogout } from '../../hooks/useLogout';
import { Captions, IconNames, pathnames } from "../../constants";
import { Icon } from '../../components/Icon/Icon';
import { Loader } from '../../components/Loader/Loader';

import './Account.css';

interface ProfileField {
    name: string;
    value: string | undefined;
    editable: boolean;
}

export interface Profile {

    id: string;

    email: string;

    firstName: string;

    lastName: string;

    avatar: string;

}

export const Account: FunctionComponent = () => {
    const { logoutData, logoutFetch } = useLogout();
    const { apiMethodState, fetchData } = useApiMethod<Profile, undefined>(accountsApiDeclaration.current);
    const [editedFieldName, setEditedFieldName] = useState('');
    const [editedFieldValue, setEditedFieldValue] = useState('');

    const {
        process: {
            loading,
            error
        },
        data
    } = apiMethodState;

    useEffect(() => {
        fetchData(undefined);
    }, [fetchData]);

    useEffect(() => {
        if (!data) {
            return;
        }
    }, [data]);

    useEffect(() => {
        if (!logoutData) {
            return;
        }

        window.location.assign(pathnames.signIn)
    }, [logoutData])

    const handleLogout = () => {
        logoutFetch(undefined)
    }

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

    const handleEditedFieldSave = () => {
        console.log('field save', editedFieldName, editedFieldValue);
        handleEditedFieldCancel();
    };

    const fields: ProfileField[] = [
        {
            name: 'id',
            value: data?.id,
            editable: false,
        },
        {
            name: 'email',
            value: data?.email,
            editable: false,
        },
        {
            name: 'first name',
            value: data?.firstName,
            editable: true,
        },
        {
            name: 'last name',
            value: data?.lastName,
            editable: true,
        },
    ];

    if (loading) {
        return (
            <>
                <p>{Captions.Loading}...</p>
                <Loader />
            </>
        );
    }

    return (
        <div className='account-page'>
            <div className="avatar">
                <img src={data?.avatar ? data?.avatar : defaultAvatarImage} alt="avatar" />
            </div>
            <table className="user-data-table">
                {fields.map(({ name, value }) => (
                    <tr key={name}>
                        <td className="bold left">{name}</td>
                        {name !== editedFieldName ? (
                            <td className="right">
                                <div className="field-value">{value || Captions.Unknown}</div>
                                <div className="field-action" onClick={() => handleEditField(name, value || '')}>
                                    <Icon name={IconNames.Create} />
                                </div>
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

            <button className="exit-button danger" onClick={handleLogout}>{Captions.Logout}</button>
        </div>
    );
};
