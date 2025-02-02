// 유틸리티 함수들
function truncateText(text, maxLength) {
    if (!text) return '정보없음';
    return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
}

function formatDate(dateStr) {
    if (!dateStr) return '정보없음';
    return `${dateStr.substring(0,4)}.${dateStr.substring(4,6)}.${dateStr.substring(6,8)}`;
}

function formatPrice(price) {
    if (!price) return '정보없음';
    return Number(price).toLocaleString() + '원';
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function getGridSizeByZoom(zoom) {
    const gridSizes = {
        7: 400,
        8: 300,
        9: 200,
        10: 150,
        11: 100,
        12: 80,
        13: 60,
        14: 40
    };
    return gridSizes[zoom] || 100;
}

// MarkerClustering 클래스
class MarkerClustering {
    constructor(options) {
        this.map = options.map;
        this.markers = [];
        this.clusters = [];
        this.minClusterSize = options.minClusterSize || 2;

        this.gridSizes = {
            4: 2,
            5: 3,
            6: 4,
            7: 6,
            8: 8,
            9: 16,
            10: 32,
            11: 64,
            12: 128,
            13: 256,
            14: 512
        };

        this.setMarkers(options.markers);
    }

    setMarkers(markers) {
        this.clearClusters();
        this.markers = markers;
        this.redraw();
    }

    clearClusters() {
        this.clusters.forEach(cluster => {
            if (cluster.marker) {
                cluster.marker.setMap(null);
            }
        });
        this.clusters = [];
        this.markers.forEach(marker => marker.setMap(null));
    }

    redraw() {
        const zoom = this.map.getZoom();
        this.clearClusters();

        if (zoom >= 16) {
            this.markers.forEach(marker => {
                marker.setMap(this.map);
                this.setupMarkerClickEvent(marker);
            });
            return;
        }

        const bounds = this.map.getBounds();
        const divisions = this.gridSizes[zoom] || 32;

        const latSpan = bounds.getNE().lat() - bounds.getSW().lat();
        const lngSpan = bounds.getNE().lng() - bounds.getSW().lng();
        const latUnit = latSpan / divisions;
        const lngUnit = lngSpan / divisions;

        const clustersMap = new Map();

        this.markers.filter(marker => bounds.hasLatLng(marker.getPosition())).forEach(marker => {
            const pos = marker.getPosition();
            const latIndex = Math.floor((pos.lat() - bounds.getSW().lat()) / latUnit);
            const lngIndex = Math.floor((pos.lng() - bounds.getSW().lng()) / lngUnit);
            const key = `${latIndex}_${lngIndex}`;

            if (!clustersMap.has(key)) {
                clustersMap.set(key, {
                    markers: [],
                    position: pos,
                    facilities: []
                });
            }
            clustersMap.get(key).markers.push(marker);
            clustersMap.get(key).facilities.push(...marker.facilities);
        });

        if (zoom <= 8) {
            const superClustersMap = new Map();

            clustersMap.forEach((cluster, key) => {
                if (cluster.markers.length >= this.minClusterSize) {
                    const [latIndex, lngIndex] = key.split('_').map(Number);
                    const superLatIndex = Math.floor(latIndex / 2);
                    const superLngIndex = Math.floor(lngIndex / 2);
                    const superKey = `${superLatIndex}_${superLngIndex}`;

                    if (!superClustersMap.has(superKey)) {
                        superClustersMap.set(superKey, {
                            markers: [],
                            position: cluster.position,
                            facilities: []
                        });
                    }
                    superClustersMap.get(superKey).markers.push(...cluster.markers);
                    superClustersMap.get(superKey).facilities.push(...cluster.facilities);
                }
            });

            superClustersMap.forEach(cluster => {
                if (cluster.markers.length >= this.minClusterSize) {
                    this.createClusterMarker(cluster);
                }
            });
        } else {
            clustersMap.forEach(cluster => {
                if (cluster.markers.length >= this.minClusterSize) {
                    this.createClusterMarker(cluster);
                } else {
                    cluster.markers.forEach(marker => {
                        marker.setMap(this.map);
                        this.setupMarkerClickEvent(marker);
                    });
                }
            });
        }
    }

    setupMarkerClickEvent(marker) {
        naver.maps.Event.addListener(marker, 'click', () => {
            if (currentInfoWindow) {
                currentInfoWindow.close();
            }

            const mapHeight = map.getSize().height;
            const offset = mapHeight * 0.15;

            const markerPosition = marker.getPosition();
            const projectionCoord = map.getProjection().fromCoordToOffset(markerPosition);
            projectionCoord.y -= offset;
            const offsetPosition = map.getProjection().fromOffsetToCoord(projectionCoord);

            let isDragging = false;

            const dragStartListener = naver.maps.Event.addListener(map, 'dragstart', () => {
                isDragging = true;
                naver.maps.Event.removeListener(dragStartListener);
            });

            const startPos = map.getCenter();
            const startTime = Date.now();
            const duration = 500;

            const easeOutCubic = t => {
                const t1 = t - 1;
                return t1 * t1 * t1 + 1;
            };

            let lastFrame = null;
            const animate = (currentTime) => {
                if (isDragging) return;

                if (!lastFrame) lastFrame = currentTime;
                const deltaTime = currentTime - lastFrame;
                lastFrame = currentTime;

                const elapsed = Date.now() - startTime;
                const progress = Math.min(elapsed / duration, 1);

                if (progress < 1) {
                    const easeProgress = easeOutCubic(progress);

                    const lat = startPos.lat() + (offsetPosition.lat() - startPos.lat()) * easeProgress;
                    const lng = startPos.lng() + (offsetPosition.lng() - startPos.lng()) * easeProgress;

                    map.setCenter(new naver.maps.LatLng(lat, lng));
                    requestAnimationFrame(animate);
                }
            };

            requestAnimationFrame(animate);

            const facilities = marker.facilities;
            if (!facilities || facilities.length === 0) return;

            const totalFacilities = facilities.length;
            const mainFacility = facilities[0];
            const markerId = `marker_${marker.__uniqueId}`;

            window[`facilities_${markerId}`] = facilities;

            const infoContent = createInfoWindowContent(
                mainFacility,
                false,
                markerId,
                totalFacilities
            );

            const infoWindow = new naver.maps.InfoWindow({
                content: infoContent,
                borderWidth: 0,
                backgroundColor: 'white',
                anchorSize: new naver.maps.Size(12, 12),
                anchorSkew: true,
                pixelOffset: new naver.maps.Point(0, -5)
            });

            window[`toggleDetail_${markerId}`] = function() {
                const isDetailView = !toggleStates[markerId];
                toggleStates[markerId] = isDetailView;
                const currentIndex = currentSelectedPrograms[markerId] || 0;

                infoWindow.setContent(createInfoWindowContent(
                    facilities[currentIndex],
                    isDetailView,
                    markerId,
                    facilities.length
                ));
            };

            infoWindow.open(map, marker);
            currentInfoWindow = infoWindow;
        });
    }

    createClusterMarker(cluster) {
        const centerLat = cluster.markers.reduce((sum, marker) =>
            sum + marker.getPosition().lat(), 0) / cluster.markers.length;
        const centerLng = cluster.markers.reduce((sum, marker) =>
            sum + marker.getPosition().lng(), 0) / cluster.markers.length;

        const clusterCenter = new naver.maps.LatLng(centerLat, centerLng);

        const clusterMarker = new naver.maps.Marker({
            position: clusterCenter,
            map: this.map,
            icon: {
                content: this.createClusterIcon(cluster.facilities.length),
                size: new naver.maps.Size(40, 40),
                anchor: new naver.maps.Point(20, 20)
            }
        });

        naver.maps.Event.addListener(clusterMarker, 'click', () => {
            const newZoom = this.map.getZoom() + 2;
            if (newZoom <= this.map.getOptions().maxZoom) {
                this.map.setZoom(newZoom);
            }
            this.map.setCenter(clusterCenter);
        });

        this.clusters.push({ marker: clusterMarker, markers: cluster.markers });
    }

    createClusterIcon(count) {
        let className = 'cluster-1';
        if (count >= 100) className = 'cluster-3';
        else if (count >= 30) className = 'cluster-2';

        const displayCount = count >= 1000 ?
            (Math.round(count/100)/10) + 'K' :
            count;

        return `<div class="cluster ${className}">${displayCount}</div>`;
    }
}

// 전역 변수 선언
let map = null;
let markers = [];
let currentInfoWindow = null;
let clustering = null;
const dataCache = new Map();
let toggleStates = {};
let currentSelectedPrograms = {};
let currentPositionMarker = null;
let currentPositionCircle = null;
let watchPositionId = null;

function initializeMap() {
    map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(36.3, 127.8),
        zoom: 7,
        minZoom: 6,
        maxZoom: 21,
        zoomControl: true,
        zoomControlOptions: {
            position: naver.maps.Position.RIGHT_CENTER
        }
    });

    // 현재 위치 버튼 추가
    const locationButton = document.createElement('div');
    locationButton.className = 'location-button';
    locationButton.innerHTML = `
        <svg viewBox="0 0 24 24">
            <path d="M12 8c-2.21 0-4 1.79-4 4s1.79 4 4 4 4-1.79 4-4-1.79-4-4-4zm8.94 3A8.994 8.994 0 0 0 13 3.06V1h-2v2.06A8.994 8.994 0 0 0 3.06 11H1v2h2.06A8.994 8.994 0 0 0 11 20.94V23h2v-2.06A8.994 8.994 0 0 0 20.94 13H23v-2h-2.06zM12 19c-3.87 0-7-3.13-7-7s3.13-7 7-7 7 3.13 7 7-3.13 7-7 7z"/>
        </svg>
    `;
    locationButton.addEventListener('click', moveToCurrentLocation);
    map.getElement().appendChild(locationButton);

    // 스타일 추가
    const style = document.createElement('style');
    style.textContent = `
        .location-button.loading {
            animation: rotate 1s linear infinite;
            opacity: 0.7;
        }
        
        @keyframes rotate {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
        }

        @keyframes pulse {
            0% {
                transform: scale(1);
                opacity: 1;
            }
            70% {
                transform: scale(2);
                opacity: 0;
            }
            100% {
                transform: scale(1);
                opacity: 0;
            }
        }
    `;
    document.head.appendChild(style);

    initializeMapEventListeners();
    loadMarkers();
}

// 현재 위치로 이동하는 함수
function moveToCurrentLocation() {
    if (!navigator.geolocation) {
        showErrorMessage('위치 정보가 지원되지 않는 브라우저입니다.');
        return;
    }

    const locationButton = document.querySelector('.location-button');
    locationButton.style.backgroundColor = '#e1e1e1';
    locationButton.classList.add('loading');

    let watchId = null;

    const timeoutPromise = new Promise((_, reject) => {
        setTimeout(() => {
            if (watchId) navigator.geolocation.clearWatch(watchId);
            reject(new Error('TIMEOUT'));
        }, 10000);
    });

    const locationPromise = new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(
            resolve,
            () => {
                watchId = navigator.geolocation.watchPosition(
                    (position) => {
                        if (watchId) navigator.geolocation.clearWatch(watchId);
                        resolve(position);
                    },
                    reject,
                    {
                        enableHighAccuracy: false,
                        timeout: 8000,
                        maximumAge: 30000
                    }
                );
            },
            {
                enableHighAccuracy: false,
                timeout: 5000,
                maximumAge: 30000
            }
        );
    });
    Promise.race([locationPromise, timeoutPromise])
        .then((position) => {
            const currentLocation = new naver.maps.LatLng(
                position.coords.latitude,
                position.coords.longitude
            );

            // 기존 현재 위치 마커와 원 제거
            if (currentPositionMarker) {
                currentPositionMarker.setMap(null);
            }
            if (currentPositionCircle) {
                currentPositionCircle.setMap(null);
            }

            // 정확도를 표시하는 원 생성
            currentPositionCircle = new naver.maps.Circle({
                map: map,
                center: currentLocation,
                radius: position.coords.accuracy || 50,
                strokeColor: '#4285F4',
                strokeOpacity: 0.3,
                strokeWeight: 1,
                fillColor: '#4285F4',
                fillOpacity: 0.1
            });

            // 현재 위치 마커 생성
            currentPositionMarker = new naver.maps.Marker({
                position: currentLocation,
                map: map,
                icon: {
                    content: `
                        <div style="width: 16px; height: 16px; position: relative;">
                            <div style="width: 16px; height: 16px; background: #4285F4; border: 2px solid white; border-radius: 50%; box-shadow: 0 0 5px rgba(0,0,0,0.3); box-sizing: border-box;">
                                <div style="width: 100%; height: 100%; background: #4285F4; border-radius: 50%; animation: pulse 2s infinite;"></div>
                            </div>
                        </div>
                    `,
                    anchor: new naver.maps.Point(8, 8)
                }
            });

            // 실시간 위치 추적 시작
            if (watchPositionId) {
                navigator.geolocation.clearWatch(watchPositionId);
            }

            watchPositionId = navigator.geolocation.watchPosition(
                (newPosition) => {
                    const newLocation = new naver.maps.LatLng(
                        newPosition.coords.latitude,
                        newPosition.coords.longitude
                    );

                    currentPositionMarker.setPosition(newLocation);
                    currentPositionCircle.setCenter(newLocation);
                    currentPositionCircle.setRadius(newPosition.coords.accuracy || 50);
                },
                (error) => {
                    console.warn('위치 추적 오류:', error);
                },
                {
                    enableHighAccuracy: false,
                    maximumAge: 5000,
                    timeout: 10000
                }
            );

            // 부드러운 이동 애니메이션
            map.panTo(currentLocation, {
                duration: 500,
                easing: 'easeOutCubic'
            });

            setTimeout(() => {
                map.setZoom(15);
            }, 500);
        })
        .catch((error) => {
            let errorMessage = '위치 정보를 가져오는데 실패했습니다.';

            if (error.message === 'TIMEOUT') {
                errorMessage = '위치 정보 요청이 시간 초과되었습니다.\n다음 방법을 시도해보세요:\n'
                    + '1. 브라우저의 위치 권한을 확인해주세요\n'
                    + '2. GPS 신호가 잘 잡히는 곳으로 이동해보세요\n'
                    + '3. 인터넷 연결을 확인해주세요';
            } else {
                switch(error.code) {
                    case error.PERMISSION_DENIED:
                        errorMessage = '위치 정보 접근이 거부되었습니다.\n'
                            + '브라우저 설정에서 위치 접근 권한을 허용해주세요.';
                        break;
                    case error.POSITION_UNAVAILABLE:
                        errorMessage = '위치 정보를 사용할 수 없습니다.\n'
                            + '1. GPS가 켜져있는지 확인해주세요\n'
                            + '2. 실외에서 다시 시도해보세요';
                        break;
                }
            }
            showErrorMessage(errorMessage, 5000);
        })
        .finally(() => {
            locationButton.style.backgroundColor = 'white';
            locationButton.classList.remove('loading');
            if (watchId) navigator.geolocation.clearWatch(watchId);
        });
}

// 마커 로드 함수
const loadMarkers = debounce(function() {
    const bounds = map.getBounds();
    const zoom = map.getZoom();

    if (zoom <= 6) return;

    const cacheKey = `${bounds.toString()}_${zoom}`;

    if(dataCache.has(cacheKey)) {
        updateMarkersAndClusters(dataCache.get(cacheKey));
        return;
    }

    $.ajax({
        url: '/api/facilities',
        data: {
            minLat: bounds.getSW().lat(),
            maxLat: bounds.getNE().lat(),
            minLng: bounds.getSW().lng(),
            maxLng: bounds.getNE().lng(),
            zoom: zoom
        },
        success: function(facilities) {
            dataCache.set(cacheKey, facilities);
            updateMarkersAndClusters(facilities);
        },
        error: function(xhr, status, error) {
            console.error("데이터 로딩 에러:", error);
        }
    });
}, 500);

// 마커 업데이트 함수
function updateMarkersAndClusters(facilities) {
    if(clustering) {
        clustering.clearClusters();
    }

    const locationGroups = new Map();

    facilities.forEach(facility => {
        if(!facility.latitude || !facility.longitude) return;

        const key = `${facility.latitude}_${facility.longitude}`;

        if (!locationGroups.has(key)) {
            locationGroups.set(key, {
                position: new naver.maps.LatLng(facility.latitude, facility.longitude),
                facilities: []
            });
        }
        locationGroups.get(key).facilities.push(facility);
    });

    const newMarkers = Array.from(locationGroups.values()).map(group => {
        const marker = new naver.maps.Marker({
            position: group.position
        });
        marker.facilities = group.facilities;
        return marker;
    });

    clustering = new MarkerClustering({
        map: map,
        markers: newMarkers,
        minClusterSize: 2,
        gridSize: getGridSizeByZoom(map.getZoom())
    });
}

// 정보창 컨텐츠 생성 함수
function createInfoWindowContent(facility, showDetail, markerId, totalFacilities) {
    const facilities = window[`facilities_${markerId}`];
    const remainingPrograms = totalFacilities - 1;
    const currentIndex = currentSelectedPrograms[markerId] || 0;
    const currentFacility = facilities[currentIndex];

    return `
    <div style="padding:15px;min-width:280px;border-radius:6px;box-shadow:0 2px 6px rgba(0,0,0,0.1);font-family:'Noto Sans KR', sans-serif;background:white;">
        <h3 style="margin:0 0 15px 0;font-size:16px;color:#333;border-bottom:2px solid #2196F3;padding-bottom:8px;">
            ${truncateText(currentFacility.FCLTY_NM, 30)}
            ${remainingPrograms > 0 ?
        `<span style="font-size:13px;color:#2196F3;margin-left:8px;cursor:pointer;text-decoration:underline;"
                     onclick="toggleFacilityList('${markerId}')" id="facilityCount_${markerId}">
                    이 외에 진행중인 프로그램: ${remainingPrograms}개
                </span>`
        : ''}
        </h3>

        <div style="position:relative;">
            <div id="facilityList_${markerId}" style="display:none;position:absolute;top:0;right:-280px;
                width:250px;background:white;padding:15px;border-radius:6px;box-shadow:0 2px 6px rgba(0,0,0,0.1);">
                <h4 style="margin:0 0 10px 0;font-size:14px;color:#1976D2;border-bottom:1px solid #e0e0e0;padding-bottom:8px;">
                    진행중인 프로그램 목록
                </h4>
                <div style="max-height:300px;overflow-y:auto;">
                    ${facilities.map((f, index) => `
                        <div onclick="showFacilityDetail('${markerId}', ${index})"
                            style="padding:8px;margin:4px 0;background:#f8f9fa;border-radius:4px;cursor:pointer;
                                   transition:all 0.2s ease;font-size:13px;hover:background-color:#e3f2fd;">
                            ${truncateText(f.PROGRM_NM, 40)}
                        </div>
                    `).join('')}
                </div>
            </div>

            <div style="background:#f8f9fa;padding:12px;border-radius:6px;margin-bottom:12px;">
                <p style="margin:0;font-size:13px;color:#444;">
                    <strong style="color:#1976D2;">프로그램:</strong>
                    <span style="margin-left:4px;">${truncateText(currentFacility.PROGRM_NM, 40)}</span>
                </p>
            </div>

            <div style="margin-bottom:12px;">
                <p style="margin:8px 0;font-size:13px;color:#555;display:flex;">
                    <span style="min-width:45px;color:#666;"><strong>지역</strong></span>
                    <span style="margin-left:8px;">${currentFacility.CTPRVN_NM} ${currentFacility.SIGNGU_NM}</span>
                </p>
                <p style="margin:8px 0;font-size:13px;color:#555;display:flex;">
                    <span style="min-width:45px;color:#666;"><strong>주소</strong></span>
                    <span style="margin-left:8px;">${currentFacility.FCLTY_ADDR || '정보없음'}</span>
                </p>
                <p style="margin:8px 0;font-size:13px;color:#555;display:flex;">
                    <span style="min-width:45px;color:#666;"><strong>전화</strong></span>
                    <span style="margin-left:8px;">${currentFacility.FCLTY_TEL_NO || '정보없음'}</span>
                </p>
            </div>

            ${showDetail ? `
                <div style="background:#fff;border:1px solid #e0e0e0;border-radius:6px;padding:12px;margin:15px 0;">
                    <h4 style="margin:0 0 10px 0;font-size:14px;color:#1976D2;">프로그램 정보</h4>
                    <p style="margin:5px 0;font-size:13px;"><strong>대상:</strong> ${currentFacility.PROGRM_TRGET_NM || '정보없음'}</p>
                    <p style="margin:5px 0;font-size:13px;"><strong>요일:</strong> ${currentFacility.PROGRM_ESTBL_WKDAY_NM || '정보없음'}</p>
                    <p style="margin:5px 0;font-size:13px;"><strong>시간:</strong> ${currentFacility.PROGRM_ESTBL_TIZN_VALUE || '정보없음'}</p>
                    <p style="margin:5px 0;font-size:13px;"><strong>기간:</strong> ${formatDate(currentFacility.PROGRM_BEGIN_DE)} ~ ${formatDate(currentFacility.PROGRM_END_DE)}</p>
                    <p style="margin:5px 0;font-size:13px;"><strong>가격:</strong> <span style="color:#1976D2;font-weight:bold">${formatPrice(currentFacility.PROGRM_PRC)}</span></p>
                    <p style="margin:5px 0;font-size:13px;"><strong>모집인원:</strong> ${currentFacility.PROGRM_RCRIT_NMPR_CO || 0}명</p>
                </div>
            ` : ''}

            <div style="display:flex;justify-content:flex-start;align-items:center;margin-top:15px;padding-top:12px;border-top:1px solid #eee;">
                <div style="display:flex;gap:8px;width:100%;">
                    <button onclick="window.toggleDetail_${markerId}()"
                            style="padding:6px 12px;background:#2196F3;color:white;border:none;border-radius:4px;font-size:13px;cursor:pointer;flex:1;white-space:nowrap;">
                        ${showDetail ? '기본 정보만 보기' : '간단 정보 더보기'}
                    </button>
                    <a href="/program/detail/${currentFacility.PROGRM_ID}"
                       style="padding:6px 12px;background:#1976D2;color:white;border:none;border-radius:4px;font-size:13px;cursor:pointer;text-decoration:none;flex:1;text-align:center;white-space:nowrap;">
                        상세페이지 이동
                    </a>
                    ${currentFacility.HMPG_URL ? `
                        <a href="${currentFacility.HMPG_URL}" target="_blank"
                           style="padding:6px 12px;background:#666;color:white;text-decoration:none;border-radius:4px;font-size:13px;flex:1;text-align:center;white-space:nowrap;">
                            홈페이지 방문
                        </a>
                    ` : ''}
                </div>
            </div>
        </div>
    </div>`;
}

// 시설 목록 토글 함수
function toggleFacilityList(markerId) {
    const listElement = document.getElementById(`facilityList_${markerId}`);
    if (listElement) {
        const isVisible = listElement.style.display !== 'none';
        listElement.style.display = isVisible ? 'none' : 'block';
    }
}
// 특정 시설의 상세 정보 표시 함수
function showFacilityDetail(markerId, index) {
    const facilities = window[`facilities_${markerId}`];
    if (!facilities || !facilities[index]) return;

    currentSelectedPrograms[markerId] = index;
    const infoWindow = currentInfoWindow;
    if (!infoWindow) return;

    toggleStates[markerId] = false;

    infoWindow.setContent(createInfoWindowContent(
        facilities[index],
        false,
        markerId,
        facilities.length
    ));
}

// 이벤트 리스너 초기화
function initializeMapEventListeners() {
    naver.maps.Event.addListener(map, 'idle', loadMarkers);
    naver.maps.Event.addListener(map, 'click', () => {
        if (currentInfoWindow) {
            currentInfoWindow.close();
            currentInfoWindow = null;
        }
    });

    window.addEventListener('resize', () => {
        if (map) {
            map.refresh();
            if (clustering) {
                clustering.redraw();
            }
        }
    });

    // 페이지 종료 시 위치 추적 정리
    window.addEventListener('beforeunload', () => {
        if (watchPositionId) {
            navigator.geolocation.clearWatch(watchPositionId);
        }
    });
}

// 에러 메시지 표시 함수
function showErrorMessage(message, duration = 3000) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;

    Object.assign(errorDiv.style, {
        position: 'fixed',
        top: '20px',
        left: '50%',
        transform: 'translateX(-50%)',
        backgroundColor: '#f44336',
        color: 'white',
        padding: '12px 24px',
        borderRadius: '4px',
        zIndex: '9999',
        boxShadow: '0 2px 5px rgba(0,0,0,0.2)',
        transition: 'opacity 0.3s ease'
    });

    document.body.appendChild(errorDiv);

    setTimeout(() => {
        errorDiv.style.opacity = '0';
        setTimeout(() => {
            document.body.removeChild(errorDiv);
        }, 300);
    }, duration);
}

// 페이지 로드 시 지도 초기화
document.addEventListener('DOMContentLoaded', initializeMap);