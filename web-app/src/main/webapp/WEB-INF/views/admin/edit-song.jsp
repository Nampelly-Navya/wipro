<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Song</title>
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webjars/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Poppins', sans-serif; }
        body { background: #f5f7fa; min-height: 100vh; }
        .sidebar { background: white; min-height: 100vh; padding: 20px; position: fixed; width: 260px; box-shadow: 2px 0 10px rgba(0,0,0,0.05); border-right: 1px solid #eee; }
        .sidebar-brand { font-size: 1.4rem; color: #e74c3c; padding: 20px 0; border-bottom: 1px solid #eee; font-weight: 600; }
        .nav-link { color: #5a6a7a; padding: 12px 15px; border-radius: 10px; margin: 5px 0; font-weight: 500; transition: all 0.3s; }
        .nav-link:hover, .nav-link.active { background: #e74c3c; color: white; }
        .nav-link i { width: 25px; }
        .main-content { margin-left: 260px; padding: 30px; }
        .form-card { background: white; border-radius: 15px; padding: 30px; box-shadow: 0 5px 20px rgba(0,0,0,0.05); border: 1px solid #eee; }
        .form-control, .form-select { background: #f8f9fa; border: 2px solid #e9ecef; color: #2c3e50; padding: 12px 15px; border-radius: 10px; }
        .form-control:focus, .form-select:focus { background: white; color: #2c3e50; border-color: #e74c3c; box-shadow: 0 0 0 3px rgba(231,76,60,0.1); }
        .form-label { color: #2c3e50; font-weight: 500; }
        h2 { color: #2c3e50; }
        
        /* File upload styling */
        .file-upload-area { 
            border: 2px dashed #e9ecef; 
            border-radius: 15px; 
            padding: 30px; 
            text-align: center; 
            transition: all 0.3s;
            cursor: pointer;
            background: #f8f9fa;
        }
        .file-upload-area:hover { border-color: #e74c3c; background: #fff5f4; }
        .file-upload-area.dragover { border-color: #e74c3c; background: #fff5f4; }
        .file-upload-area.has-file { border-color: #27ae60; background: #f0fff4; }
        .file-upload-area i { font-size: 2.5rem; color: #bdc3c7; margin-bottom: 10px; }
        .file-upload-area.has-file i { color: #27ae60; }
        .file-upload-area p { color: #7f8c8d; margin: 0; }
        .file-upload-area .file-name { color: #27ae60; font-weight: 500; margin-top: 10px; }
        .audio-preview { margin-top: 15px; width: 100%; }
        .upload-section { margin-bottom: 20px; }
        .upload-section h6 { color: #2c3e50; margin-bottom: 15px; font-weight: 600; }
        .current-file { background: #e8f5e9; border-radius: 10px; padding: 15px; margin-bottom: 15px; }
        .current-file i { color: #27ae60; }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-brand"><i class="fas fa-music me-2"></i>Music Library</div>
        <nav class="nav flex-column mt-4">
            <a class="nav-link" href="/admin"><i class="fas fa-tachometer-alt me-2"></i>Dashboard</a>
            <a class="nav-link active" href="/admin/songs"><i class="fas fa-compact-disc me-2"></i>Songs</a>
            <a class="nav-link" href="/admin/songs/add"><i class="fas fa-plus-circle me-2"></i>Add Song</a>
            <a class="nav-link" href="/admin/notifications"><i class="fas fa-bell me-2"></i>Notifications</a>
            <hr style="border-color: #eee;">
            <a class="nav-link" href="/"><i class="fas fa-home me-2"></i>Back to Home</a>
            <a class="nav-link" href="/admin/logout"><i class="fas fa-sign-out-alt me-2"></i>Logout</a>
        </nav>
    </div>
    
    <div class="main-content">
        <h2 class="mb-4"><i class="fas fa-edit me-2" style="color: #e74c3c;"></i>Edit Song</h2>
        
        <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
        <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
        
        <div class="form-card">
            <!-- Song Details Form -->
            <form action="/admin/songs/edit/${song.songId}" method="post">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Song Name *</label>
                        <input type="text" class="form-control" name="songName" value="${song.songName}" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Album Name</label>
                        <input type="text" class="form-control" name="albumName" value="${song.albumName}">
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Singer *</label>
                        <input type="text" class="form-control" name="singer" value="${song.singer}" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Music Director *</label>
                        <input type="text" class="form-control" name="musicDirector" value="${song.musicDirector}" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Release Date</label>
                        <input type="date" class="form-control" name="releaseDate" value="${song.releaseDate}">
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Type</label>
                        <select class="form-select" name="songType">
                            <option value="FREE" ${song.songType == 'FREE' ? 'selected' : ''}>Free</option>
                            <option value="PREMIUM" ${song.songType == 'PREMIUM' ? 'selected' : ''}>Premium</option>
                        </select>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Status</label>
                        <select class="form-select" name="songStatus">
                            <option value="AVAILABLE" ${song.songStatus == 'AVAILABLE' ? 'selected' : ''}>Available</option>
                            <option value="NOTAVAILABLE" ${song.songStatus == 'NOTAVAILABLE' ? 'selected' : ''}>Not Available</option>
                        </select>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary"><i class="fas fa-save me-2"></i>Update Song Details</button>
                <a href="/admin/songs" class="btn btn-secondary">Cancel</a>
            </form>
            
            <hr class="my-4">
            
            <!-- Audio Upload Section -->
            <h5 class="mb-4"><i class="fas fa-file-audio me-2" style="color: #e74c3c;"></i>Audio & Cover Files</h5>
            
            <div class="row">
                <div class="col-md-6 upload-section">
                    <h6><i class="fas fa-music me-2"></i>Audio File</h6>
                    
                    <c:if test="${not empty song.audioUrl}">
                        <div class="current-file">
                            <i class="fas fa-check-circle me-2"></i>
                            <strong>Current:</strong> ${song.audioUrl}
                            <audio controls class="audio-preview mt-2">
                                <source src="/admin/songs/audio-stream/${song.songId}" type="audio/mpeg">
                            </audio>
                        </div>
                    </c:if>
                    
                    <form action="/admin/songs/${song.songId}/upload-audio" method="post" enctype="multipart/form-data">
                        <div class="file-upload-area" id="audioUploadArea" onclick="document.getElementById('audioFile').click()">
                            <i class="fas fa-cloud-upload-alt" id="audioIcon"></i>
                            <p id="audioText">${empty song.audioUrl ? 'Click to upload audio' : 'Click to replace audio'}</p>
                            <p class="file-name" id="audioFileName" style="display: none;"></p>
                        </div>
                        <input type="file" id="audioFile" name="file" accept="audio/*" style="display: none;" onchange="handleFileSelect(this, 'audio')">
                        <audio id="audioPreview" controls class="audio-preview" style="display: none;"></audio>
                        <button type="submit" class="btn btn-success mt-3" id="uploadAudioBtn" style="display: none;">
                            <i class="fas fa-upload me-2"></i>Upload Audio
                        </button>
                    </form>
                </div>
                
                <div class="col-md-6 upload-section">
                    <h6><i class="fas fa-image me-2"></i>Cover Image</h6>
                    
                    <c:if test="${not empty song.coverImageUrl}">
                        <div class="current-file">
                            <i class="fas fa-check-circle me-2"></i>
                            <strong>Current:</strong> ${song.coverImageUrl}
                            <br>
                            <img src="/admin/songs/cover-stream/${song.songId}" class="mt-2 rounded" style="max-width: 100px; max-height: 100px;">
                        </div>
                    </c:if>
                    
                    <form action="/admin/songs/${song.songId}/upload-cover" method="post" enctype="multipart/form-data">
                        <div class="file-upload-area" id="coverUploadArea" onclick="document.getElementById('coverFile').click()">
                            <i class="fas fa-image" id="coverIcon"></i>
                            <p id="coverText">${empty song.coverImageUrl ? 'Click to upload cover' : 'Click to replace cover'}</p>
                            <p class="file-name" id="coverFileName" style="display: none;"></p>
                        </div>
                        <input type="file" id="coverFile" name="file" accept="image/*" style="display: none;" onchange="handleFileSelect(this, 'cover')">
                        <img id="coverPreview" class="mt-3 rounded" style="max-width: 100%; max-height: 150px; display: none;">
                        <button type="submit" class="btn btn-success mt-3" id="uploadCoverBtn" style="display: none;">
                            <i class="fas fa-upload me-2"></i>Upload Cover
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        function handleFileSelect(input, type) {
            const file = input.files[0];
            if (!file) return;
            
            const uploadArea = document.getElementById(type + 'UploadArea');
            const icon = document.getElementById(type + 'Icon');
            const text = document.getElementById(type + 'Text');
            const fileName = document.getElementById(type + 'FileName');
            const uploadBtn = document.getElementById('upload' + (type.charAt(0).toUpperCase() + type.slice(1)) + 'Btn');
            
            uploadArea.classList.add('has-file');
            text.style.display = 'none';
            fileName.style.display = 'block';
            fileName.textContent = file.name;
            uploadBtn.style.display = 'inline-block';
            
            if (type === 'audio') {
                icon.className = 'fas fa-check-circle';
                const preview = document.getElementById('audioPreview');
                preview.src = URL.createObjectURL(file);
                preview.style.display = 'block';
            } else {
                icon.className = 'fas fa-check-circle';
                const preview = document.getElementById('coverPreview');
                preview.src = URL.createObjectURL(file);
                preview.style.display = 'block';
            }
        }
        
        // Drag and drop support
        ['audioUploadArea', 'coverUploadArea'].forEach(id => {
            const area = document.getElementById(id);
            const type = id.replace('UploadArea', '');
            
            area.addEventListener('dragover', (e) => {
                e.preventDefault();
                area.classList.add('dragover');
            });
            
            area.addEventListener('dragleave', () => {
                area.classList.remove('dragover');
            });
            
            area.addEventListener('drop', (e) => {
                e.preventDefault();
                area.classList.remove('dragover');
                const input = document.getElementById(type + 'File');
                input.files = e.dataTransfer.files;
                handleFileSelect(input, type);
            });
        });
    </script>
</body>
</html>
