import 'package:flutter/material.dart';
import 'package:pdf/pdf.dart';
import 'package:pdf/widgets.dart' as pw;
import 'package:printing/printing.dart';

class NeededDocumentsScreen extends StatelessWidget {
  final List<Map<String, dynamic>> documentList = [
    {
      'title': 'Demande d\'Autorisation de Construire',
      'documents': [
        'Formulaire de demande rempli.',
        'Copie de la carte d\'identité du demandeur.',
        'Titre de propriété ou acte de possession du terrain.',
        'Plan de masse et plans architecturaux (visés par un architecte agréé).',
        'Rapport technique (études de sol ou géotechniques si nécessaire).',
        'Certificat de conformité aux règlements d\'urbanisme.',
        'Attestation de paiement des taxes ou droits liés à la construction.',
      ],
    },
    // Ajoutez ici les autres types de demandes avec leurs documents...
    {
      'title': 'Demande d\'Autorisation d\'Exploitation Commerciale',
      'documents': [
        'Copie de la carte d\'identité ou passeport du demandeur.',
        'Justificatif de domicile ou adresse professionnelle.',
        'Titre de propriété ou contrat de bail du local commercial.',
        'Certificat d\'enregistrement au registre de commerce.',
        'Plan des locaux (si applicable).',
        'Attestation de conformité sanitaire ou environnementale (selon le secteur).',
        'Assurance responsabilité civile professionnelle.',
      ],
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Ce que vous avez besoin'),
        backgroundColor: Colors.purple,
      ),
      body: ListView.builder(
        itemCount: documentList.length,
        itemBuilder: (context, index) {
          final item = documentList[index];
          return ExpansionTile(
            title: Text(item['title']),
            children: (item['documents'] as List<String>)
                .map((doc) => ListTile(title: Text(doc)))
                .toList(),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          generatePDF();
        },
        backgroundColor: Colors.purple,
        child: Icon(Icons.picture_as_pdf),
      ),
    );
  }

  void generatePDF() async {
    final pdf = pw.Document();
    pdf.addPage(
      pw.Page(
        build: (context) {
          return pw.Column(
            crossAxisAlignment: pw.CrossAxisAlignment.start,
            children: documentList.map((item) {
              return pw.Column(
                crossAxisAlignment: pw.CrossAxisAlignment.start,
                children: [
                  pw.Text(item['title'], style: pw.TextStyle(fontSize: 18, fontWeight: pw.FontWeight.bold)),
                  ...item['documents'].map((doc) => pw.Text('- $doc', style: pw.TextStyle(fontSize: 12))).toList(),
                  pw.SizedBox(height: 10),
                ],
              );
            }).toList(),
          );
        },
      ),
    );

    // Imprime ou sauvegarde le PDF
    await Printing.layoutPdf(
      onLayout: (format) async => pdf.save(),
    );
  }
}