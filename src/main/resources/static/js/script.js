document.addEventListener('DOMContentLoaded', function() {
    let map = L.map('map').setView([44.3278, 23.7943], 13);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    const defaultMarker = L.marker([44.3278, 23.7943])
        .addTo(map)
        .bindPopup("<b>Default Location</b>");

    const stationMarkers = L.featureGroup();

    stationData.stations.forEach(function(station1) {
        if (station1.latitude && station1.longitude) {
            const marker = L.marker([station1.latitude, station1.longitude])
                .bindPopup('<b>' + station1.stationName +
                    '</b><br>' + station1.stationAddress + '<br>' +
                    '<div class="d-flex justify-content-between"> ' +
                    '<a href="/agent/' + station1.id + '/create" class="btn-primary">Add agent</a> ' +
                    '<a href="/station/' + station1.id + '/hierarchy" class="btn-primary">View agent hierarchy</a> ' +
                    '</div>'
                );
            stationMarkers.addLayer(marker);

            window.stationData.stations.forEach(function(station2) {
                if (station1 !== station2 && station2.latitude && station2.longitude) {
                    const latlng1 = L.latLng(station1.latitude, station1.longitude);
                    const latlng2 = L.latLng(station2.latitude, station2.longitude);
                    const distance = latlng1.distanceTo(latlng2);
                    const distanceKm = (distance / 1000).toFixed(2);

                    L.polyline([latlng1, latlng2], { color: 'blue' })
                        .bindTooltip(distanceKm + ' km', { permanent: false, direction: 'center' })
                        .addTo(map);
                }
            });
        }
    });

    stationMarkers.addTo(map);

    if (stationData.stations.length > 0) {
        const allMarkers = L.featureGroup([defaultMarker, stationMarkers]);
        map.fitBounds(allMarkers.getBounds(), {
            padding: [50, 50],
            maxZoom: 15
        });
    } else {
        map.setView([stationData.defaultLocation.lat, stationData.defaultLocation.lng], 13);
    }
});

document.addEventListener('DOMContentLoaded', function() {
    const corpsSelect = document.getElementById('corps');
    const rankSelect = document.getElementById('rank');

    if (corpsSelect && rankSelect) {
        corpsSelect.addEventListener('change', function() {
            while (rankSelect.options.length > 1) {
                rankSelect.remove(1);
            }

            if (this.value === 'Officer') {
                addRankOption(rankSelect, 'General_Quaestor', 'General-Quaestor');
                addRankOption(rankSelect, 'Chief_Quaestor', 'Chief-Quaestor');
                addRankOption(rankSelect, 'Principal_Quaestor ', 'Principal Quaestor');
                addRankOption(rankSelect, 'Quaestor', 'Quaestor');
                addRankOption(rankSelect, 'Chief_Commissioner', 'Chief-Commissioner');
                addRankOption(rankSelect, 'Commissioner', 'Commissioner');
                addRankOption(rankSelect, 'Sub_Commissioner', 'Sub-Commissioner');
                addRankOption(rankSelect, 'Principal_Inspector', 'Principal Inspector');
                addRankOption(rankSelect, 'Inspector', 'Inspector');
                addRankOption(rankSelect, 'Sub_Inspector', 'Sub-Inspector');
            } else if (this.value === 'Agent') {
                addRankOption(rankSelect, 'Principal_Chief_Agent', 'Principal Chief Agent');
                addRankOption(rankSelect, 'Chief_Agent', 'Chief Agent');
                addRankOption(rankSelect, 'Deputy_Chief_Agent', 'Deputy Chief Agent');
                addRankOption(rankSelect, 'Principal_Agent', 'Principal Agent');
                addRankOption(rankSelect, 'Agent', 'Agent');
            }
        });

        if (corpsSelect.value) {
            corpsSelect.dispatchEvent(new Event('change'));
        }
    }

    function addRankOption(selectElement, value, text) {
        const option = document.createElement('option');
        option.value = value;
        option.text = text;
        selectElement.add(option);
    }
});