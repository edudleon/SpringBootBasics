package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserMapper userMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    public int createCredential(CredentialForm credential, String username) {
        String key = encryptionService.getSecureKey();
        return credentialMapper.insert(new Credential(null, credential.getUrl(), credential.getUsername(), key, encryptionService.encryptValue(credential.getPassword(), key), userMapper.getUser(username).getUserId()));
    }

    public List<Credential> getCredentialsByUser(String username){
        return credentialMapper.findCredentialsByUserId(userMapper.getUser(username).getUserId());
    }

    public void deleteCredential(Long credentialId){
        credentialMapper.delete(credentialId);
    }
}
