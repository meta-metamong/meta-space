<template>
    <button class="location-btn" @click="findLocation"><i class="bi bi-crosshair"></i> 현재 위치로 이동</button>
    <div class="w-100">
        <div id="map"></div>
    </div>
</template>

<script>
import { toRaw } from "vue";
import { get } from "../apis/axios";
export default {
    name: "KakaoMap",
    data() {
        return {
            fctInfo: [],
            markerPositions: [],
            markers: [],
            clusterer: null,
            overlays: [],
            lat: "",
            lon: "",
        };
    },
    mounted() {
        if (window.kakao && window.kakao.maps) {
            this.initMap();
        } else {
            const script = document.createElement("script");
            script.onload = () => kakao.maps.load(this.initMap);
            script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_KEY}&libraries=clusterer`;
            document.head.appendChild(script);
        }
        this.getFctInfo();
        this.saveLocation();
    },
    methods: {
        initMap() {
            const container = document.getElementById("map");
            const options = {
                center: new kakao.maps.LatLng(37.583842, 126.999969),
                level: 5,
            };
            this.map = new kakao.maps.Map(container, options);
            kakao.maps.event.addListener(this.map, "tilesloaded", () => {
                this.initClusterer();
                this.displayMarkers();
            });
        },
        initClusterer() {
            this.clusterer = new kakao.maps.MarkerClusterer({
                map: this.map,
                averageCenter: true,
                minLevel: 10,
                disableClickZoom: true,
            });
        },
        displayMarkers() {
            if (this.markers.length > 0) {
                this.markers.forEach((marker) => marker.setMap(null));
                this.overlays.forEach((overlay) => overlay.setMap(null));
            }

            this.markers = [];
            this.overlays = [];

            this.fctInfo.forEach((fct) => {
                let marker = new kakao.maps.Marker({
                    position: new kakao.maps.LatLng(fct.fctLatitude, fct.fctLongitude),
                    map: this.map,
                });

                let overlayContent = document.createElement("div");
                overlayContent.className = "wrap";
                overlayContent.innerHTML = `
                    <div class="info">
                        <div class="title">
                            ${fct.fctName}
                            <div class="close" title="닫기"></div>
                        </div>
                        <div class="body">
                            <div class="img">
                                <img src="${fct.repImgUrl}" width="73" height="70">
                            </div>
                            <div class="desc">
                                <div class="jibun ellipsis">${fct.catName}</div>
                                <div class="ellipsis">${fct.fctAddress} ${fct.fctDetailAddress}</div>
                                <div class="ellipsis">${fct.fctTel}</div>
                                <div><a href="#" class="link">바로가기</a></div>
                            </div>
                        </div>
                    </div>
                `;

                let overlay = new kakao.maps.CustomOverlay({
                    content: overlayContent,
                    position: marker.getPosition(),
                    map: null,
                });

                // 닫기 버튼 클릭 이벤트 추가
                overlayContent.querySelector(".close").addEventListener("click", () => {
                    overlay.setMap(null);
                });

                overlayContent.querySelector(".link").addEventListener("click", () => {
                    this.$router.push(`/facilities/${fct.fctId}`)
                });

                // 마커 클릭 시 해당 오버레이 표시, 기존 오버레이 닫기
                kakao.maps.event.addListener(marker, "click", () => {
                    this.overlays.forEach((o) => o.setMap(null));
                    overlay.setMap(this.map);
                });

                this.markers.push(marker);
                this.overlays.push(overlay);
            });

            this.clusterer.addMarkers(this.markers);

            kakao.maps.event.addListener(this.clusterer, "clusterclick", (cluster) => {
                var level = this.map.getLevel() - 2;
                toRaw(this.map).setLevel(level, { anchor: cluster.getCenter() });
            });
        },
        saveLocation(){
            this.lat = this.$store.state.loc.lat; // 위도
            this.lon = this.$store.state.loc.lon; // 경도
        },
        findLocation() {
            var locPosition = new kakao.maps.LatLng(this.lat, this.lon); // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
            this.map.setCenter(locPosition);
        },
        async getFctInfo() {
            const response = await get(`/facilities?center-latitude=${this.lat}&center-longitude=${this.lon}&page-size=100`);
            this.fctInfo = response.data.content.facilities;
            this.markerPositions = this.fctInfo.map(fct => [fct.fctLatitude, fct.fctLongitude]);

            console.log(this.fctInfo);
        }
    },
};
</script>

<style>
#map {
    /* height: 600px; */
    height: 800px;
}

.wrap {
    position: absolute;
    left: 0;
    bottom: 40px;
    width: 288px;
    height: 147px;
    margin-left: -144px;
    text-align: left;
    overflow: hidden;
    font-size: 12px;
    font-family: 'Malgun Gothic', dotum, '돋움', sans-serif;
    line-height: 1.5;
}

.wrap * {
    padding: 0;
    margin: 0;
}

.wrap .info {
    width: 286px;
    height: 135px;
    border-radius: 5px;
    border-bottom: 2px solid #ccc;
    border-right: 1px solid #ccc;
    overflow: hidden;
    background: #fff;
}

.wrap .info:nth-child(1) {
    border: 0;
    box-shadow: 0px 1px 2px #888;
}

.info .title {
    padding: 5px 0 2px 10px;
    /* height: 30px; */
    background: #eee;
    border-bottom: 1px solid #ddd;
    font-size: 18px;
    font-weight: bold;
}

.info .close {
    position: absolute;
    top: 10px;
    right: 10px;
    color: #888;
    width: 17px;
    height: 17px;
    background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/overlay_close.png');
}

.info .close:hover {
    cursor: pointer;
}

.info .body {
    position: relative;
    overflow: hidden;
}

.info .desc {
    position: relative;
    margin: 13px 0 0 90px;
    /* height: 75px; */
}

.desc .ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-bottom: 3px;
}

.desc .jibun {
    font-size: 11px;
    color: #888;
    margin-top: -2px;
}

.info .img {
    position: absolute;
    top: 6px;
    left: 5px;
    width: 73px;
    height: 71px;
    border: 1px solid #ddd;
    color: #888;
    overflow: hidden;
}

.info:after {
    content: '';
    position: absolute;
    margin-left: -12px;
    left: 50%;
    bottom: 0;
    width: 22px;
    height: 12px;
    background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png')
}

.info .link {
    color: #5085BB;
}

.location-btn {
    background: #fff;
    color: #000;
    /* border: 1px solid #5085BB; */
    border-radius: 5px;
    margin: 0px 0 8px 10px;
    padding: 5px 15px;
}
</style>
