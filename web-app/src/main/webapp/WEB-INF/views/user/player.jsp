<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Music Player</title>
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webjars/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Poppins', sans-serif; }
        body { background: #f5f7fa; min-height: 100vh; }
        .sidebar { background: white; min-height: 100vh; padding: 20px; position: fixed; width: 260px; box-shadow: 2px 0 10px rgba(0,0,0,0.05); border-right: 1px solid #eee; }
        .sidebar-brand { font-size: 1.4rem; color: #3498db; padding: 20px 0; border-bottom: 1px solid #eee; font-weight: 600; }
        .nav-link { color: #5a6a7a; padding: 12px 15px; border-radius: 10px; margin: 5px 0; font-weight: 500; transition: all 0.3s; }
        .nav-link:hover, .nav-link.active { background: #3498db; color: white; }
        .nav-link i { width: 25px; }
        .main-content { margin-left: 260px; padding: 30px; padding-bottom: 200px; }
        
        /* Player Container */
        .player-container { 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); 
            border-radius: 30px; 
            padding: 40px; 
            text-align: center; 
            margin-bottom: 30px; 
            color: white;
            box-shadow: 0 20px 60px rgba(102, 126, 234, 0.4);
        }
        .album-art { 
            width: 200px; 
            height: 200px; 
            background: rgba(255,255,255,0.2); 
            border-radius: 50%; 
            display: flex; 
            align-items: center; 
            justify-content: center; 
            margin: 0 auto 25px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.3);
            overflow: hidden;
            transition: transform 0.3s;
        }
        .album-art.playing { animation: rotate 8s linear infinite; }
        .album-art img { width: 100%; height: 100%; object-fit: cover; }
        @keyframes rotate { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
        
        /* Audio Controls */
        .audio-controls { margin-top: 30px; }
        .progress-container { 
            width: 100%; 
            height: 6px; 
            background: rgba(255,255,255,0.3); 
            border-radius: 3px; 
            cursor: pointer;
            margin: 20px 0;
        }
        .progress-bar-audio { 
            height: 100%; 
            background: white; 
            border-radius: 3px; 
            width: 0%; 
            transition: width 0.1s;
        }
        .time-display { 
            display: flex; 
            justify-content: space-between; 
            font-size: 0.85rem; 
            opacity: 0.8;
            margin-bottom: 20px;
        }
        
        .player-btn { 
            width: 55px; 
            height: 55px; 
            border-radius: 50%; 
            border: none; 
            font-size: 1.2rem; 
            cursor: pointer; 
            transition: all 0.3s;
            background: rgba(255,255,255,0.2);
            color: white;
        }
        .player-btn:hover { background: rgba(255,255,255,0.3); transform: scale(1.1); }
        .play-btn { 
            width: 80px; 
            height: 80px; 
            background: white; 
            color: #667eea; 
            font-size: 1.8rem;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
        }
        .play-btn:hover { transform: scale(1.15); background: white; }
        
        /* Volume Control */
        .volume-control { 
            display: flex; 
            align-items: center; 
            justify-content: center; 
            gap: 10px;
            margin-top: 20px;
        }
        .volume-slider { 
            width: 100px; 
            height: 4px; 
            -webkit-appearance: none; 
            background: rgba(255,255,255,0.3); 
            border-radius: 2px;
        }
        .volume-slider::-webkit-slider-thumb { 
            -webkit-appearance: none; 
            width: 14px; 
            height: 14px; 
            background: white; 
            border-radius: 50%; 
            cursor: pointer;
        }
        
        /* Song List */
        .song-list { background: white; border-radius: 20px; padding: 25px; box-shadow: 0 5px 30px rgba(0,0,0,0.05); border: 1px solid #eee; }
        .song-item { 
            display: flex; 
            align-items: center; 
            padding: 15px; 
            border-radius: 15px; 
            cursor: pointer; 
            transition: all 0.3s;
            margin-bottom: 8px;
        }
        .song-item:hover { background: #f8f9fa; transform: translateX(5px); }
        .song-item.active { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
        .song-item.active strong, .song-item.active p { color: white; }
        .song-item.active .song-icon { background: rgba(255,255,255,0.2); color: white; }
        .song-item strong { color: #2c3e50; font-weight: 600; }
        .song-item p { color: #7f8c8d; margin: 0; }
        .song-icon { 
            width: 45px; 
            height: 45px; 
            background: #f0f3ff; 
            border-radius: 12px; 
            display: flex; 
            align-items: center; 
            justify-content: center;
            color: #667eea;
            margin-right: 15px;
        }
        .song-item .play-indicator { 
            width: 35px; 
            height: 35px; 
            border-radius: 50%; 
            background: #667eea; 
            color: white; 
            display: flex; 
            align-items: center; 
            justify-content: center;
            opacity: 0;
            transition: opacity 0.3s;
        }
        .song-item:hover .play-indicator, .song-item.active .play-indicator { opacity: 1; }
        .song-item.active .play-indicator { background: white; color: #667eea; }
        
        .no-audio-badge { 
            font-size: 0.7rem; 
            background: #f39c12; 
            color: white; 
            padding: 2px 8px; 
            border-radius: 10px;
            margin-left: 10px;
        }
        
        h2, h5 { color: #2c3e50; font-weight: 600; }
        
        /* Toast notification */
        .toast-container { position: fixed; bottom: 30px; right: 30px; z-index: 1000; }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-brand"><i class="fas fa-music me-2"></i>Music Library</div>
        <nav class="nav flex-column mt-4">
            <a class="nav-link" href="/user/dashboard"><i class="fas fa-tachometer-alt me-2"></i>Dashboard</a>
            <a class="nav-link" href="/user/songs"><i class="fas fa-music me-2"></i>Browse Songs</a>
            <a class="nav-link" href="/user/playlists"><i class="fas fa-list me-2"></i>My Playlists</a>
            <a class="nav-link active" href="/user/player"><i class="fas fa-play-circle me-2"></i>Player</a>
            <hr style="border-color: #eee;">
            <a class="nav-link" href="/"><i class="fas fa-home me-2"></i>Back to Home</a>
            <a class="nav-link" href="/user/logout"><i class="fas fa-sign-out-alt me-2"></i>Logout</a>
        </nav>
    </div>
    
    <div class="main-content">
        <h2 class="mb-4"><i class="fas fa-play-circle me-2" style="color: #667eea;"></i>Music Player</h2>
        
        <div class="player-container">
            <div class="album-art" id="albumArt">
                <i class="fas fa-music fa-4x" id="defaultIcon"></i>
                <img id="coverImage" src="" style="display: none;">
            </div>
            <h3 id="currentSong">Select a song to play</h3>
            <p class="opacity-75 mb-0" id="currentArtist">--</p>
            
            <div class="audio-controls">
                <div class="progress-container" id="progressContainer">
                    <div class="progress-bar-audio" id="progressBar"></div>
                </div>
                <div class="time-display">
                    <span id="currentTime">0:00</span>
                    <span id="duration">0:00</span>
                </div>
                
                <div class="d-flex justify-content-center align-items-center gap-4">
                    <button class="player-btn" onclick="previousSong()" title="Previous"><i class="fas fa-step-backward"></i></button>
                    <button class="player-btn play-btn" onclick="togglePlay()" id="playBtn" title="Play/Pause">
                        <i class="fas fa-play" id="playIcon"></i>
                    </button>
                    <button class="player-btn" onclick="nextSong()" title="Next"><i class="fas fa-step-forward"></i></button>
                </div>
                
                <div class="volume-control">
                    <i class="fas fa-volume-down"></i>
                    <input type="range" class="volume-slider" id="volumeSlider" min="0" max="1" step="0.1" value="0.7">
                    <i class="fas fa-volume-up"></i>
                </div>
            </div>
            
            <!-- Hidden Audio Element -->
            <audio id="audioPlayer" preload="auto"></audio>
        </div>
        
        <div class="row">
            <div class="col-lg-8 mb-4">
                <div class="song-list">
                    <h5 class="mb-4"><i class="fas fa-music me-2" style="color: #667eea;"></i>Available Songs</h5>
                    <c:forEach items="${songs}" var="song" varStatus="status">
                        <div class="song-item" 
                             data-id="${song.songId}" 
                             data-name="${song.songName}" 
                             data-singer="${song.singer}"
                             data-audio="${song.audioUrl}"
                             data-cover="${song.coverImageUrl}"
                             data-index="${status.index}"
                             onclick="playSong(this)">
                            <div class="song-icon">
                                <i class="fas fa-music"></i>
                            </div>
                            <div class="flex-grow-1">
                                <strong>${song.songName}</strong>
                                <c:if test="${empty song.audioUrl}">
                                    <span class="no-audio-badge">No Audio</span>
                                </c:if>
                                <p class="small mb-0">${song.singer} • ${song.albumName}</p>
                            </div>
                            <span class="badge ${song.songType == 'PREMIUM' ? 'bg-warning text-dark' : 'bg-success'} me-3">${song.songType}</span>
                            <div class="play-indicator">
                                <i class="fas fa-play"></i>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty songs}">
                        <div class="text-center py-5" style="color: #7f8c8d;">
                            <i class="fas fa-music fa-3x mb-3"></i>
                            <p>No songs available</p>
                        </div>
                    </c:if>
                </div>
            </div>
            <div class="col-lg-4 mb-4">
                <div class="song-list">
                    <h5 class="mb-4"><i class="fas fa-info-circle me-2" style="color: #667eea;"></i>Now Playing</h5>
                    <div id="nowPlayingInfo" class="text-center py-4" style="color: #7f8c8d;">
                        <i class="fas fa-headphones fa-3x mb-3"></i>
                        <p>Select a song to start playing</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Toast Container -->
    <div class="toast-container">
        <div id="toast" class="toast" role="alert">
            <div class="toast-body" id="toastMessage"></div>
        </div>
    </div>
    
    <script src="/webjars/jquery/3.7.1/jquery.min.js"></script>
    <script src="/webjars/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
    <script>
        const audioPlayer = document.getElementById('audioPlayer');
        const playIcon = document.getElementById('playIcon');
        const albumArt = document.getElementById('albumArt');
        const progressBar = document.getElementById('progressBar');
        const progressContainer = document.getElementById('progressContainer');
        const currentTimeEl = document.getElementById('currentTime');
        const durationEl = document.getElementById('duration');
        const volumeSlider = document.getElementById('volumeSlider');
        
        let currentSongElement = null;
        let songList = [];
        let currentIndex = -1;
        
        // Initialize song list
        document.querySelectorAll('.song-item').forEach((item, index) => {
            songList.push({
                id: item.dataset.id,
                name: item.dataset.name,
                singer: item.dataset.singer,
                audio: item.dataset.audio,
                cover: item.dataset.cover,
                element: item
            });
        });
        
        function playSong(element) {
            const songId = element.dataset.id;
            const songName = element.dataset.name;
            const singer = element.dataset.singer;
            const audioUrl = element.dataset.audio;
            const coverUrl = element.dataset.cover;
            currentIndex = parseInt(element.dataset.index);
            
            // Update UI
            document.querySelectorAll('.song-item').forEach(item => item.classList.remove('active'));
            element.classList.add('active');
            currentSongElement = element;
            
            document.getElementById('currentSong').textContent = songName;
            document.getElementById('currentArtist').textContent = singer;
            
            // Update cover image
            const coverImage = document.getElementById('coverImage');
            const defaultIcon = document.getElementById('defaultIcon');
            if (coverUrl && coverUrl !== 'null' && coverUrl !== '') {
                coverImage.src = '/user/songs/' + songId + '/cover';
                coverImage.style.display = 'block';
                defaultIcon.style.display = 'none';
            } else {
                coverImage.style.display = 'none';
                defaultIcon.style.display = 'block';
            }
            
            // Update now playing info
            document.getElementById('nowPlayingInfo').innerHTML = `
                <div class="text-center">
                    <h5 style="color: #2c3e50;">${songName}</h5>
                    <p style="color: #7f8c8d;">${singer}</p>
                    <hr>
                    <p class="small" style="color: #95a5a6;">
                        <i class="fas fa-music me-2"></i>Now Playing
                    </p>
                </div>
            `;
            
            // Check if audio is available
            if (!audioUrl || audioUrl === 'null' || audioUrl === '') {
                showToast('This song does not have an audio file uploaded yet', 'warning');
                return;
            }
            
            // Load and play audio
            audioPlayer.src = '/user/songs/' + songId + '/stream';
            audioPlayer.load();
            audioPlayer.play().then(() => {
                playIcon.classList.remove('fa-play');
                playIcon.classList.add('fa-pause');
                albumArt.classList.add('playing');
            }).catch(err => {
                console.error('Playback failed:', err);
                showToast('Failed to play audio', 'error');
            });
        }
        
        function togglePlay() {
            if (!audioPlayer.src || audioPlayer.src === window.location.href) {
                showToast('Please select a song first', 'info');
                return;
            }
            
            if (audioPlayer.paused) {
                audioPlayer.play();
                playIcon.classList.remove('fa-play');
                playIcon.classList.add('fa-pause');
                albumArt.classList.add('playing');
            } else {
                audioPlayer.pause();
                playIcon.classList.remove('fa-pause');
                playIcon.classList.add('fa-play');
                albumArt.classList.remove('playing');
            }
        }
        
        function previousSong() {
            if (songList.length === 0) return;
            currentIndex = currentIndex > 0 ? currentIndex - 1 : songList.length - 1;
            playSong(songList[currentIndex].element);
        }
        
        function nextSong() {
            if (songList.length === 0) return;
            currentIndex = currentIndex < songList.length - 1 ? currentIndex + 1 : 0;
            playSong(songList[currentIndex].element);
        }
        
        // Audio event listeners
        audioPlayer.addEventListener('timeupdate', () => {
            const progress = (audioPlayer.currentTime / audioPlayer.duration) * 100;
            progressBar.style.width = progress + '%';
            currentTimeEl.textContent = formatTime(audioPlayer.currentTime);
        });
        
        audioPlayer.addEventListener('loadedmetadata', () => {
            durationEl.textContent = formatTime(audioPlayer.duration);
        });
        
        audioPlayer.addEventListener('ended', () => {
            nextSong();
        });
        
        audioPlayer.addEventListener('error', (e) => {
            console.error('Audio error:', e);
            showToast('Error loading audio file', 'error');
            playIcon.classList.remove('fa-pause');
            playIcon.classList.add('fa-play');
            albumArt.classList.remove('playing');
        });
        
        // Progress bar click
        progressContainer.addEventListener('click', (e) => {
            const rect = progressContainer.getBoundingClientRect();
            const percent = (e.clientX - rect.left) / rect.width;
            audioPlayer.currentTime = percent * audioPlayer.duration;
        });
        
        // Volume control
        volumeSlider.addEventListener('input', (e) => {
            audioPlayer.volume = e.target.value;
        });
        audioPlayer.volume = 0.7;
        
        function formatTime(seconds) {
            if (isNaN(seconds)) return '0:00';
            const mins = Math.floor(seconds / 60);
            const secs = Math.floor(seconds % 60);
            return mins + ':' + (secs < 10 ? '0' : '') + secs;
        }
        
        function showToast(message, type = 'info') {
            const toast = document.getElementById('toast');
            const toastMessage = document.getElementById('toastMessage');
            toastMessage.textContent = message;
            
            const colors = {
                success: '#27ae60',
                warning: '#f39c12',
                error: '#e74c3c',
                info: '#3498db'
            };
            toast.style.background = colors[type] || colors.info;
            toast.style.color = 'white';
            
            const bsToast = new bootstrap.Toast(toast, { delay: 3000 });
            bsToast.show();
        }
    </script>
</body>
</html>
