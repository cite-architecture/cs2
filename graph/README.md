# CITE GRAPH

## Implementation Notes

### CTS URNs

Group-level URN: works, versions, and exemplars. Because why not? 

Work-level URN, no citation: versions and exemplars.

Work-level URN, citation: all version- and exemplar level URNs offering that
citation; anything indexed to them (see below).

Version-level URN, citation: All CTS data; substrings; anything indexed to them; exemplars. 

Individal containing citations (non-leaf-nodes): Anything indexed to that citation, which will include all contained citations.

Ranges (including mixed-ranges of container and leaf-node): All individual leaf nodes, and anything indexed to them. 

### CITE Image

Image URN: anything it illustrates (including versions with ROIs); all ROIs.

Image URN w/ ROI: the image, any other image-ROI that touches this; anything indexed to any of those!



## Testing

Integration tests based on the dataset at `../fuseki/graph.ttl`

Run `gradle farmIntegrationTest`.

<urn:cts:greekLit:tlg0012.tlg001:> : returns six records.

