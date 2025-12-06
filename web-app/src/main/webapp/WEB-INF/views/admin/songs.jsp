<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Songs</title>
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
        .table { background: white; border-radius: 15px; overflow: hidden; box-shadow: 0 5px 20px rgba(0,0,0,0.05); }
        .table thead { background: #f8f9fa; }
        .table th { color: #2c3e50; font-weight: 600; border-bottom: 2px solid #eee; padding: 15px; }
        .table td { color: #5a6a7a; padding: 15px; vertical-align: middle; border-bottom: 1px solid #f0f0f0; }
        .table tbody tr:hover { background: #fafafa; }
        
        /* Modal styling - White theme */
        .modal-content { background: white; border: 1px solid #eee; border-radius: 20px; }
        .modal-header { border-bottom: 1px solid #eee; padding: 20px 25px; }
        .modal-header h5 { color: #2c3e50; font-weight: 600; }
        .modal-footer { border-top: 1px solid #eee; }
        
        .song-detail-card { background: #f8f9fa; border-radius: 15px; padding: 25px; }
        .song-icon { font-size: 80px; color: #e74c3c; margin-bottom: 20px; }
        .detail-row { padding: 12px 0; border-bottom: 1px solid #eee; }
        .detail-row:last-child { border-bottom: none; }
        .detail-label { color: #7f8c8d; font-size: 0.9rem; margin-bottom: 3px; }
        .detail-value { color: #2c3e50; font-size: 1.1rem; font-weight: 500; }
        h2 { color: #2c3e50; }
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
        <div class="d-flex justify-content-between mb-4">
            <h2><i class="fas fa-compact-disc me-2" style="color: #e74c3c;"></i>Manage Songs</h2>
            <div>
                <a href="/admin/songs/sync-all" class="btn btn-success me-2" onclick="return confirm('Sync all songs to User Service?')">
                    <i class="fas fa-sync me-2"></i>Sync All to Users
                </a>
                <a href="/admin/songs/add" class="btn btn-danger"><i class="fas fa-plus me-2"></i>Add Song</a>
            </div>
        </div>
        
        <c:if test="${param.success != null}">
            <div class="alert alert-success alert-dismissible fade show">
                ${param.success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${param.error != null}">
            <div class="alert alert-danger alert-dismissible fade show">
                ${param.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <div class="table-responsive">
            <table class="table">
                <thead><tr><th>ID</th><th>Name</th><th>Singer</th><th>Album</th><th>Audio</th><th>Type</th><th>Status</th><th>Actions</th></tr></thead>
                <tbody>
                    <c:forEach items="${songs}" var="song">
                        <tr>
                            <td>${song.songId}</td>
                            <td><i class="fas fa-music me-2" style="color: #e74c3c;"></i>${song.songName}</td>
                            <td>${song.singer}</td>
                            <td>${song.albumName}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty song.audioUrl}">
                                        <span class="badge bg-success" title="Audio uploaded"><i class="fas fa-check me-1"></i>Yes</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary" title="No audio"><i class="fas fa-times me-1"></i>No</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><span class="badge ${song.songType == 'PREMIUM' ? 'bg-warning text-dark' : 'bg-info'}">${song.songType}</span></td>
                            <td><span class="badge ${song.songStatus == 'AVAILABLE' ? 'bg-success' : 'bg-secondary'}">${song.songStatus}</span></td>
                            <td>
                                <!-- View Details Button -->
                                <button type="button" class="btn btn-sm btn-info" 
                                        data-bs-toggle="modal" 
                                        data-bs-target="#viewSongModal"
                                        data-song-id="${song.songId}"
                                        data-song-name="${song.songName}"
                                        data-song-singer="${song.singer}"
                                        data-song-album="${song.albumName}"
                                        data-song-director="${song.musicDirector}"
                                        data-song-date="${song.releaseDate}"
                                        data-song-type="${song.songType}"
                                        data-song-status="${song.songStatus}"
                                        title="View Details">
                                    <i class="fas fa-eye"></i>
                                </button>
                                <a href="/admin/songs/edit/${song.songId}" class="btn btn-sm btn-primary" title="Edit"><i class="fas fa-edit"></i></a>
                                <a href="/admin/songs/delete/${song.songId}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this song?')" title="Delete"><i class="fas fa-trash"></i></a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    
    <!-- View Song Details Modal -->
    <div class="modal fade" id="viewSongModal" tabindex="-1" aria-labelledby="viewSongModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="viewSongModalLabel">
                        <i class="fas fa-music me-2" style="color: #e74c3c;"></i>Song Details
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="song-detail-card">
                        <div class="row">
                            <div class="col-md-4 text-center">
                                <i class="fas fa-compact-disc song-icon"></i>
                                <h4 id="modalSongName" style="color: #e74c3c;" class="mb-2"></h4>
                                <span id="modalSongType" class="badge bg-info"></span>
                            </div>
                            <div class="col-md-8">
                                <div class="detail-row">
                                    <div class="detail-label"><i class="fas fa-hashtag me-2"></i>Song ID</div>
                                    <div class="detail-value" id="modalSongId"></div>
                                </div>
                                <div class="detail-row">
                                    <div class="detail-label"><i class="fas fa-microphone me-2"></i>Singer / Artist</div>
                                    <div class="detail-value" id="modalSinger"></div>
                                </div>
                                <div class="detail-row">
                                    <div class="detail-label"><i class="fas fa-record-vinyl me-2"></i>Album</div>
                                    <div class="detail-value" id="modalAlbum"></div>
                                </div>
                                <div class="detail-row">
                                    <div class="detail-label"><i class="fas fa-user-tie me-2"></i>Music Director</div>
                                    <div class="detail-value" id="modalDirector"></div>
                                </div>
                                <div class="detail-row">
                                    <div class="detail-label"><i class="fas fa-calendar-alt me-2"></i>Release Date</div>
                                    <div class="detail-value" id="modalReleaseDate"></div>
                                </div>
                                <div class="detail-row">
                                    <div class="detail-label"><i class="fas fa-signal me-2"></i>Status</div>
                                    <div class="detail-value">
                                        <span id="modalSongStatus" class="badge"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Close
                    </button>
                    <a href="#" id="modalEditBtn" class="btn btn-primary">
                        <i class="fas fa-edit me-2"></i>Edit Song
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="/webjars/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
    <script>
        var viewSongModal = document.getElementById('viewSongModal');
        viewSongModal.addEventListener('show.bs.modal', function (event) {
            var button = event.relatedTarget;
            var songId = button.getAttribute('data-song-id');
            var songName = button.getAttribute('data-song-name');
            var singer = button.getAttribute('data-song-singer');
            var album = button.getAttribute('data-song-album');
            var director = button.getAttribute('data-song-director');
            var releaseDate = button.getAttribute('data-song-date');
            var songType = button.getAttribute('data-song-type');
            var songStatus = button.getAttribute('data-song-status');
            
            document.getElementById('modalSongId').textContent = songId;
            document.getElementById('modalSongName').textContent = songName;
            document.getElementById('modalSinger').textContent = singer || 'N/A';
            document.getElementById('modalAlbum').textContent = album || 'N/A';
            document.getElementById('modalDirector').textContent = director || 'N/A';
            document.getElementById('modalReleaseDate').textContent = releaseDate || 'N/A';
            
            var typeElement = document.getElementById('modalSongType');
            typeElement.textContent = songType;
            typeElement.className = 'badge ' + (songType === 'PREMIUM' ? 'bg-warning text-dark' : 'bg-info');
            
            var statusElement = document.getElementById('modalSongStatus');
            statusElement.textContent = songStatus === 'AVAILABLE' ? '✅ Available' : '❌ Not Available';
            statusElement.className = 'badge ' + (songStatus === 'AVAILABLE' ? 'bg-success' : 'bg-secondary');
            
            document.getElementById('modalEditBtn').href = '/admin/songs/edit/' + songId;
        });
    </script>
</body>
</html>
