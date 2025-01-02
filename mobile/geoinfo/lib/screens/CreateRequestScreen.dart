import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:latlong2/latlong.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:file_picker/file_picker.dart';

class CreateRequestScreen extends StatefulWidget {
  @override
  _CreateRequestScreenState createState() => _CreateRequestScreenState();
}

class _CreateRequestScreenState extends State<CreateRequestScreen> {
  final TextEditingController cinController = TextEditingController();
  final TextEditingController nameController = TextEditingController();
  final TextEditingController lastNameController = TextEditingController();
  final TextEditingController searchController = TextEditingController();
  LatLng? selectedLocation;
  String selectedAuthorizationType = "Construction";
  String? generatedID;
  String? requestDate;
  double zoomLevel = 10.0;
  MapController mapController = MapController();
  String currentMapStyle = "streets";
  String? formulairePath;
  String? planSituationPath;
  String? planMassePath;

  @override
  void initState() {
    super.initState();
    _generateRequestID();
    _setRequestDate();
  }

  // Générer un ID unique pour la demande
  void _generateRequestID() {
    setState(() {
      generatedID = DateTime.now().millisecondsSinceEpoch.toString();
    });
  }

  // Définir la date actuelle comme date de demande
  void _setRequestDate() {
    setState(() {
      requestDate = DateTime.now().toLocal().toString().split(' ')[0];
    });
  }

  // Géocodage pour rechercher une adresse
  Future<void> _searchLocation(String address) async {
    final url =
        'https://nominatim.openstreetmap.org/search?q=$address&format=json&addressdetails=1&limit=1';
    final response = await http.get(Uri.parse(url));
    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      if (data.isNotEmpty) {
        final lat = double.parse(data[0]['lat']);
        final lon = double.parse(data[0]['lon']);
        setState(() {
          selectedLocation = LatLng(lat, lon);
          mapController.move(selectedLocation!, zoomLevel);
        });
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text("Adresse introuvable."),
          ),
        );
      }
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("Erreur lors de la recherche."),
        ),
      );
    }
  }

  // Fonction pour sélectionner un fichier
  Future<void> _pickFile(String fileType) async {
    final result = await FilePicker.platform.pickFiles();
    if (result != null && result.files.isNotEmpty) {
      setState(() {
        switch (fileType) {
          case "formulaire":
            formulairePath = result.files.first.path!;
            break;
          case "planSituation":
            planSituationPath = result.files.first.path!;
            break;
          case "planMasse":
            planMassePath = result.files.first.path!;
            break;
        }
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("Fichier importé : ${result.files.first.name}"),
        ),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("Aucun fichier sélectionné."),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Créer une Demande"),
        backgroundColor: Colors.purple,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              "Informations Détails de la Demande",
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 16.0),
            TextField(
              controller: cinController,
              decoration: InputDecoration(
                labelText: "CIN",
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 16.0),
            TextField(
              controller: nameController,
              decoration: InputDecoration(
                labelText: "Nom",
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 16.0),
            TextField(
              controller: lastNameController,
              decoration: InputDecoration(
                labelText: "Prénom",
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 16.0),
            DropdownButtonFormField<String>(
              value: selectedAuthorizationType,
              items: ["Construction", "Modification", "Renovation"]
                  .map((type) => DropdownMenuItem(
                value: type,
                child: Text(type),
              ))
                  .toList(),
              onChanged: (value) {
                setState(() {
                  selectedAuthorizationType = value!;
                });
              },
              decoration: InputDecoration(
                labelText: "Type d'Autorisation",
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 16.0),
            Text(
              "Recherche d'Adresse:",
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            TextField(
              controller: searchController,
              decoration: InputDecoration(
                labelText: "Entrez une adresse",
                border: OutlineInputBorder(),
                suffixIcon: IconButton(
                  icon: Icon(Icons.search),
                  onPressed: () => _searchLocation(searchController.text),
                ),
              ),
            ),
            SizedBox(height: 16.0),
            Container(
              height: 300,
              child: FlutterMap(
                mapController: mapController,
                options: MapOptions(
                  center: LatLng(31.6300, -7.9800), // Exemple de position initiale
                  zoom: zoomLevel,
                  onTap: (tapPosition, latlng) {
                    setState(() {
                      selectedLocation = latlng;
                    });
                  },
                ),
                children: [
                  TileLayer(
                    urlTemplate: currentMapStyle == "streets"
                        ? "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        : "https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png",
                    subdomains: ['a', 'b', 'c'],
                  ),
                  if (selectedLocation != null)
                    MarkerLayer(
                      markers: [
                        Marker(
                          point: selectedLocation!,
                          builder: (ctx) => Icon(
                            Icons.location_pin,
                            color: Colors.red,
                            size: 40.0,
                          ),
                        ),
                      ],
                    ),
                ],
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                ElevatedButton(
                  onPressed: () {
                    setState(() {
                      if (zoomLevel > 1) zoomLevel--;
                      mapController.move(mapController.center, zoomLevel);
                    });
                  },
                  child: Text("Dézoomer"),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.purple,
                    foregroundColor: Colors.white,
                  ),
                ),
                ElevatedButton(
                  onPressed: () {
                    setState(() {
                      if (zoomLevel < 18) zoomLevel++;
                      mapController.move(mapController.center, zoomLevel);
                    });
                  },
                  child: Text("Zoomer"),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.purple,
                    foregroundColor: Colors.white,
                  ),
                ),
                ElevatedButton(
                  onPressed: () {
                    setState(() {
                      currentMapStyle = currentMapStyle == "streets"
                          ? "satellite"
                          : "streets";
                    });
                  },
                  child: Text("Changer Carte"),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.purple,
                    foregroundColor: Colors.white,
                  ),
                ),
              ],
            ),
            SizedBox(height: 16.0),
            ElevatedButton(
              onPressed: () => _pickFile("formulaire"),
              child: Text("Importer Formulaire Demande Permis Construire"),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.purple,
                foregroundColor: Colors.white,
              ),
            ),
            if (formulairePath != null)
              Text(
                "Formulaire : ${formulairePath!.split('/').last}",
                style: TextStyle(fontSize: 14, color: Colors.grey),
              ),
            SizedBox(height: 16.0),
            ElevatedButton(
              onPressed: () => _pickFile("planSituation"),
              child: Text("Importer Plan Situation Terrain"),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.purple,
                foregroundColor: Colors.white,
              ),
            ),
            if (planSituationPath != null)
              Text(
                "Plan Situation : ${planSituationPath!.split('/').last}",
                style: TextStyle(fontSize: 14, color: Colors.grey),
              ),
            SizedBox(height: 16.0),
            ElevatedButton(
              onPressed: () => _pickFile("planMasse"),
              child: Text("Importer Plan Masse Projet"),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.purple,
                foregroundColor: Colors.white,
              ),
            ),
            if (planMassePath != null)
              Text(
                "Plan Masse : ${planMassePath!.split('/').last}",
                style: TextStyle(fontSize: 14, color: Colors.grey),
              ),
            SizedBox(height: 16.0),
            ElevatedButton(
              onPressed: () {
                if (cinController.text.isEmpty ||
                    nameController.text.isEmpty ||
                    lastNameController.text.isEmpty ||
                    formulairePath == null ||
                    planSituationPath == null ||
                    planMassePath == null) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Text("Veuillez remplir tous les champs et importer les fichiers."),
                    ),
                  );
                } else {
                  print("CIN: ${cinController.text}");
                  print("Nom: ${nameController.text}");
                  print("Prénom: ${lastNameController.text}");
                  print("Type d'autorisation: $selectedAuthorizationType");
                  print("Formulaire: $formulairePath");
                  print("Plan Situation: $planSituationPath");
                  print("Plan Masse: $planMassePath");

                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Text("Demande créée avec succès!"),
                    ),
                  );
                }
              },
              child: Text("Créer la Demande"),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.purple,
                foregroundColor: Colors.white,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
