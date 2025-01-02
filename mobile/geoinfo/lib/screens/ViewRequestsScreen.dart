import 'package:flutter/material.dart';

class ViewRequestsScreen extends StatelessWidget {
  // Simuler des données pour les demandes
  final List<Map<String, String>> requests = [
    {
      'type': 'Construction',
      'cin': 'X123456',
      'nom': 'Ali',
      'prenom': 'Ben Salah',
      'latitude': '31.7917',
      'longitude': '-7.0926',
    },
    {
      'type': 'Villa',
      'cin': 'Y789012',
      'nom': 'Sara',
      'prenom': 'El Fassi',
      'latitude': '32.0000',
      'longitude': '-6.5000',
    },
    {
      'type': 'Terrain',
      'cin': 'Z345678',
      'nom': 'Mohamed',
      'prenom': 'Alaoui',
      'latitude': '33.5731',
      'longitude': '-7.5898',
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Consulter les Demandes"),
        backgroundColor: Colors.purple,
      ),
      body: ListView.builder(
        itemCount: requests.length,
        itemBuilder: (context, index) {
          final request = requests[index];
          return Card(
            margin: EdgeInsets.symmetric(horizontal: 10, vertical: 5),
            child: ListTile(
              leading: Icon(Icons.request_page, color: Colors.purple),
              title: Text("Type: ${request['type']}"),
              subtitle: Text(
                  "Demandeur: ${request['nom']} ${request['prenom']}\nCIN: ${request['cin']}\nLocalisation: (${request['latitude']}, ${request['longitude']})"),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => RequestDetailsScreen(request: request),
                  ),
                );
              },
            ),
          );
        },
      ),
    );
  }
}

class RequestDetailsScreen extends StatelessWidget {
  final Map<String, String> request;

  RequestDetailsScreen({required this.request});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Détails de la Demande"),
        backgroundColor: Colors.purple,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              "Type d'autorisation : ${request['type']}",
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 10),
            Text("CIN : ${request['cin']}", style: TextStyle(fontSize: 16)),
            SizedBox(height: 10),
            Text("Nom : ${request['nom']}"),
            Text("Prénom : ${request['prenom']}"),
            SizedBox(height: 10),
            Text(
                "Localisation : Latitude ${request['latitude']}, Longitude ${request['longitude']}"),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: Text("Retour"),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.purple,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
