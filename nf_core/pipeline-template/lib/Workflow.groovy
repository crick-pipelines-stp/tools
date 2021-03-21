/*
 * This file holds several functions specific to the pipeline.
 */

class Workflow {

    // Citation string
    private static String citation(workflow) {
        return "If you use ${workflow.manifest.name} for your analysis please cite:\n\n" +
               "* The pipeline\n" + 
               "  https://doi.org/10.5281/zenodo.1400710\n\n" +
               "* The nf-core framework\n" +
               "  https://doi.org/10.1038/s41587-020-0439-x\n\n" +
               "* Software dependencies\n" +
               "  https://github.com/${workflow.manifest.name}/blob/master/CITATIONS.md"
    }

    static void validate_main_params(workflow, params, json_schema, log) {
        if (params.validate_params) {
            NfcoreSchema.validateParameters(params, json_schema, log)
        }

        // Check that conda channels are set-up correctly
        if (params.enable_conda) {
            Checks.check_conda_channels(log)
        }

        // Check AWS batch settings
        Checks.aws_batch(workflow, params)

        // Check the hostnames against configured profiles
        Checks.hostname(workflow, params, log)
    }

    static void validate_workflow_params(params, log) {
        genome_exists(params, log)

        if (!params.fasta) { 
            log.error "Genome fasta file not specified with e.g. '--fasta genome.fa' or via a detectable config file."
            System.exit(1)
        }
    }

    // Exit pipeline if incorrect --genome key provided
    static void genome_exists(params, log) {
        if (params.genomes && params.genome && !params.genomes.containsKey(params.genome)) {
            log.error "=============================================================================\n" +
                      "  Genome '${params.genome}' not found in any config files provided to the pipeline.\n" +
                      "  Currently, the available genome keys are:\n" +
                      "  ${params.genomes.keySet().join(", ")}\n" +
                      "==================================================================================="
            System.exit(1)
        }
    }

    // Get attribute from genome config file e.g. fasta
    static String get_genome_attribute(params, attribute) {
        def val = ''
        if (params.genomes && params.genome && params.genomes.containsKey(params.genome)) {
            if (params.genomes[ params.genome ].containsKey(attribute)) {
                val = params.genomes[ params.genome ][ attribute ]
            }
        }
        return val
    }    
}
