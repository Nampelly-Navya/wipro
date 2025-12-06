package com.musiclibrary.admin.service;

import com.musiclibrary.admin.dto.AdminDTO;
import com.musiclibrary.admin.dto.AdminLoginRequestDTO;
import com.musiclibrary.admin.dto.AdminLoginResponseDTO;

public interface AdminService {
    AdminDTO register(AdminDTO adminDTO);
    AdminLoginResponseDTO login(AdminLoginRequestDTO request);
    AdminDTO getAdminById(Long adminId);
}




