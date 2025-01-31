var osmUrl = 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
       osmAttrib = '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
       osm = L.tileLayer(osmUrl, {maxZoom: 15, attribution: osmAttrib});
var map = L.map('map').setView([-3.996716943365505, -79.20174975448631], 15);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
   attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);
L.marker([-3.98812 , -79.20436]).addTo(map)
.bindPopup('VetSaVeterinaria')
.openPopup();
L.marker([-3.993456 , -79.201234]).addTo(map)
.bindPopup('ClínicaVeterinariaLoja')
.openPopup();
L.marker([-3.987654 , -79.198765]).addTo(map)
.bindPopup('VeterinariaSanFrancisco')
.openPopup();
L.marker([-3.985432 , -79.202345]).addTo(map)
.bindPopup('VeterinariaElArca')
.openPopup();
L.marker([-3.991234 , -79.199876]).addTo(map)
.bindPopup('VeterinariaLosAnimalitos')
.openPopup();
L.marker([-3.989876 , -79.203456]).addTo(map)
.bindPopup('VeterinariaPetCare')
.openPopup();
L.marker([-3.992345 , -79.200123]).addTo(map)
.bindPopup('VeterinariaAmigoFiel')
.openPopup();
L.marker([-3.986789 , -79.201987]).addTo(map)
.bindPopup('VeterinariaPatitasFelices')
.openPopup();
L.marker([-3.990123 , -79.198234]).addTo(map)
.bindPopup('VeterinariaCuidadoAnimal')
.openPopup();
L.marker([-3.987123 , -79.202678]).addTo(map)
.bindPopup('VeterinariaMascotasSaludables')
.openPopup();
