// map.js

// 전역 상태 관리를 위한 상수 객체
const MapState = {
    currentInfoWindow: null,
    toggleStates: {},
    markers: [],
    clustering: null,
    dataCache: new Map()
};

// 유틸리티 함수들
const formatDate = dateStr => {
    if (!dateStr) return '정보없음';
    return `${dateStr.substring(0,4)}.${dateStr.substring(4,6)}.${dateStr.substring(6,8)}`;
};

const formatPrice = price => {
    if (!price) return '정보없음';
    return Number(price).toLocaleString() + '원';
};

const truncateText = (text, maxLength) => {
    if (!text) return '정보없음';
    return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
};

// 클러스터링 클래스
class MarkerClustering {
    constructor(options) {
        this.map = options.map;
        this.markers = [];
        this.clusters = [];
        this.minClusterSize = options.minClusterSize || 2;

        this.setMarkers(options.markers);
    }

    setMarkers(markers) {
        this.clearClusters();
        this.markers = markers;
        this.redraw();
    }

    clearClusters() {
        this.clusters.forEach(cluster => {
            if (cluster.marker) cluster.marker.setMap(null);
        });
        this.clusters = [];
        this.markers.forEach(marker => marker.setMap(null));
    }

    redraw() {
        this.markers.forEach(marker => {
            marker.setMap(this.map);
        });
    }

    createClusterIcon(count) {
        let className = 'cluster-1';
        if (count >= 100) className = 'cluster-3';
        else if (count >= 30) className = 'cluster-2';
        return `<div class="cluster ${className}">${count}</div>`;
    }
}

// 마커 데이터 로드
const loadMarkers = async () => {
    try {
        const response = await fetch('/api/facilities');
        if (!response.ok) throw new Error('Failed to fetch facilities');

        const facilities = await response.json();
        console.log('Loaded facilities:', facilities.length);
        updateMarkersAndClusters(facilities);
    } catch (error) {
        console.error('Error loading markers:', error);
    }
};

// 마커 업데이트
function updateMarkersAndClusters(facilities) {
    // 기존 마커 제거
    MapState.markers.forEach(marker => marker.setMap(null));
    MapState.markers = [];

    // 새 마커 생성
    facilities.forEach(facility => {
        if (!facility.latitude || !facility.longitude) return;

        const marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(facility.latitude, facility.longitude),
            map: MapState.map
        });

        // 마커 클릭 이벤트
        naver.maps.Event.addListener(marker, 'click', () => {
            if (MapState.currentInfoWindow) {
                MapState.currentInfoWindow.close();
            }

            const infoWindow = new naver.maps.InfoWindow({
                content: createInfoWindowContent(facility),
                borderWidth: 0,
                backgroundColor: 'white',
                borderRadius: '8px',
                pixelOffset: new naver.maps.Point(0, -10)
            });

            infoWindow.open(MapState.map, marker);
            MapState.currentInfoWindow = infoWindow;
        });

        MapState.markers.push(marker);
    });
}

// 정보창 콘텐츠 생성
function createInfoWindowContent(facility) {
    return `
        <div style="padding:15px;min-width:280px;border-radius:6px;box-shadow:0 2px 6px rgba(0,0,0,0.1);font-family:'Noto Sans KR', sans-serif;background:white;">
            <h3 style="margin:0 0 15px 0;font-size:16px;color:#333;border-bottom:2px solid #2196F3;padding-bottom:8px;">
                ${facility.FCLTY_NM}
            </h3>
            <div style="background:#f8f9fa;padding:12px;border-radius:6px;margin-bottom:12px;">
                <p style="margin:0;font-size:13px;color:#444;">
                    <strong style="color:#1976D2;">프로그램:</strong>
                    <span style="margin-left:4px;">${facility.PROGRM_NM}</span>
                </p>
            </div>
            <div style="margin-bottom:12px;">
                <p style="margin:8px 0;font-size:13px;color:#555;"><strong>지역:</strong> ${facility.CTPRVN_NM} ${facility.SIGNGU_NM}</p>
                <p style="margin:8px 0;font-size:13px;color:#555;"><strong>주소:</strong> ${facility.FCLTY_ADDR || '정보없음'}</p>
                <p style="margin:8px 0;font-size:13px;color:#555;"><strong>전화:</strong> ${facility.FCLTY_TEL_NO || '정보없음'}</p>
                <p style="margin:8px 0;font-size:13px;color:#555;"><strong>운영시간:</strong> ${facility.PROGRM_ESTBL_TIZN_VALUE || '정보없음'}</p>
                <p style="margin:8px 0;font-size:13px;color:#555;"><strong>운영요일:</strong> ${facility.PROGRM_ESTBL_WKDAY_NM || '정보없음'}</p>
                <p style="margin:8px 0;font-size:13px;color:#555;"><strong>가격:</strong> ${formatPrice(facility.PROGRM_PRC)}</p>
            </div>
            ${facility.HMPG_URL ? `
                <div style="text-align:right;">
                    <a href="${facility.HMPG_URL}" target="_blank" style="padding:6px 12px;background:#666;color:white;text-decoration:none;border-radius:4px;font-size:13px;">
                        홈페이지 방문
                    </a>
                </div>
            ` : ''}
        </div>
    `;
}

// 지도 초기화
const initMap = () => {
    const mapDiv = document.getElementById('map');
    MapState.map = new naver.maps.Map(mapDiv, {
        center: new naver.maps.LatLng(37.5666805, 126.9784147),
        zoom: 10,
        minZoom: 6,
        maxZoom: 21,
        zoomControl: true,
        zoomControlOptions: {
            position: naver.maps.Position.RIGHT_CENTER
        }
    });

    loadMarkers();
};

// DOM 로드 완료 시 초기화
document.addEventListener('DOMContentLoaded', initMap);