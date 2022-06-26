import React, { Component } from 'react';
import { Document,Page } from 'react-pdf/dist/esm/entry.webpack';
import jert from './Chertezh.pdf'
import pse from './PSE-A-75-00-01-00R-720A-A.pdf'

class AllPages extends Component {
  state = { numPages: null, pageNumber: 1 };

	onDocumentLoadSuccess = ({ numPages }) => {
		this.setState({ numPages });
	};

	goToPrevPage = () => {
		if (this.state.pageNumber != 1) {
		this.setState((state) => ({ pageNumber: state.pageNumber - 1 }));
	}
	}
	goToNextPage = () => {
		if (this.state.pageNumber != this.state.numPages) {
			this.setState((state) => ({ pageNumber: state.pageNumber + 1 }));
		}
	}	

	render() {
		const { pageNumber, numPages } = this.state;
    var file;
    debugger
    if (this.props.match.params.pdfName === 'PSE-A-75-00-01-00R-720A-A.pdf') {
      file = pse
    } else {
      file = jert
    }

		return (
			<div>
				<nav>
					<button onClick={this.goToPrevPage}>Prev</button>
					<button onClick={this.goToNextPage}>Next</button>
				</nav>
        <p>
					Page {pageNumber} of {numPages}
				</p>

				<div style={{ width: 600 }}>
					<Document
						file={file}
						onLoadSuccess={this.onDocumentLoadSuccess}
            height={50}
					>
						<Page pageNumber={pageNumber} width={1000} />
					</Document>
				</div>
			</div>
		);
	}
}

export default AllPages;